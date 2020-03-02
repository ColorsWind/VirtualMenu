package com.blzeecraft.virtualmenu.core.user;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.event.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;

import lombok.val;

/**
 * 处理有关 User 打开/关闭 PacketMenu 的逻辑.
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
public class UserManager {
	public static final LogNode LOG_NODE = LogNode.of("#PacketManager");

	public static void closeInventory(IUser<?> user) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		AbstractPacketOutCloseWindow<?> packetWindowClose = adapter.createPacketCloseWindow();
		packetWindowClose.setWindowId(0);
		try {
			adapter.sendServerPacket(user, packetWindowClose);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送关闭背包 Packet 时发生异常.");
			e.printStackTrace();
		}
	}
	
	public static void openMenu(IUser<?> user, IPacketMenu menu) {
		val adapter = VirtualMenu.getPacketAdapter();
		closePacketMenu(user); // ensure packet-menu are closed.
		closeInventory(user); // ensure inventory are closed.
		UserSession session = new UserSession(user, menu).init();
		user.setCurrentSession(session);
		menu.addViewer(session);
		val packetWindowOpen = session.createPacketWindowOpen();
		val packetWindowItems = session.createPacketWindowItemsForMenu();
		try {
			adapter.sendServerPacket(user, packetWindowOpen);
			adapter.sendServerPacket(user, packetWindowItems);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送打开菜单 Packet 时发生异常.");
			e.printStackTrace();
		}
		MenuActionEvent event = new MenuActionEvent(session, EventType.OPEN_MENU);
		menu.handle(event);
	}

	public static void closePacketMenu(UserSession session) {
		val adapter = VirtualMenu.getPacketAdapter();
		// create and send packet
		AbstractPacketOutCloseWindow<?> packetWindowClose = session.createPacketWindowCloseForMenu();
		try {
			adapter.sendServerPacket(session.getUser(), packetWindowClose);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送关闭菜单 Packet 时发生异常.");
			e.printStackTrace();
		}
		handleClosePacketMenu(session);
	}
	
	public static void handleClosePacketMenu(UserSession session) {
		val menu = session.getMenu();
		val user = session.getUser();
		menu.handle(new MenuActionEvent(session, EventType.CLOSE_MENU));
		menu.removeViewer(session);
		user.setCurrentSession(null);
	}
	
	
	public static void closePacketMenu(IUser<?> user) {
		user.getCurrentSession().ifPresent(UserManager::closePacketMenu);
	}
	


	public static void closeAllMenu() {
		VirtualMenu.getUsersOnline().stream().map(IUser::getCurrentSession).filter(Optional::isPresent)
				.map(Optional::get).forEach(UserManager::closePacketMenu);
	}



}
