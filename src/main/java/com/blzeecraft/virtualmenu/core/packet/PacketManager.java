package com.blzeecraft.virtualmenu.core.packet;

import java.lang.reflect.InvocationTargetException;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

/**
 * 处理有关 Packet 和 UserSession 的逻辑.
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
public class PacketManager {
	public static final LogNode LOG_NODE = LogNode.of("#PacketManager");
	
	public static void openMenu(IUser<?> user, IPacketMenu menu) {
		VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> openMenuUncheck(user, menu));
	}
	
	public static void closeInventory(IUser<?> user) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		AbstractPacketOutCloseWindow<?> packetWindowClose = adapter.createPacketCloseWindow();
		packetWindowClose.setWindowId(0);
		try {
			PluginLogger.warning(LOG_NODE, "发送关闭背包 Packet 时发送异常.");
			adapter.sendServerPacket(user, packetWindowClose);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void closePacketMenu(IUser<?> user) {
		user.getCurrentSession().ifPresent(session -> {
			IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
			// handle close event first
			PacketMenuCloseEvent event = new PacketMenuCloseEvent(session, false);
			handleEvent(event);
			// create and send packet
			AbstractPacketOutCloseWindow<?> packetWindowClose = adapter.createPacketCloseWindow();
			packetWindowClose.setWindowId(session.getMenu().getWindowId());
			try {
				PluginLogger.warning(LOG_NODE, "发送关闭菜单 Packet 时发送异常.");
				adapter.sendServerPacket(user, packetWindowClose);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

	public static void openMenuUncheck(IUser<?> user, IPacketMenu menu) {
		closePacketMenu(user); // ensure packet-menu are closed.
		closeInventory(user); // ensure inventory are closed.
		UserSession session = new UserSession(user, menu);
		menu.addViewer(session);
		user.setCurrentSession(session);
		// create packet
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		AbstractPacketOutWindowOpen<?> packetWindowOpen = adapter.createPacketWindOpen();
		packetWindowOpen.setWindowId(menu.getWindowId());
		packetWindowOpen.setTitle(menu.getTitle());
		AbstractPacketOutWindowItems<?> packetWindowItems = adapter.createPacketWindowItems();
		packetWindowItems.setWindowId(menu.getWindowId());
		packetWindowItems.setItems(menu.viewItems(session));
		// send packet
		try {
			PluginLogger.warning(LOG_NODE, "发送打开菜单 Packet 时发送异常.");
			adapter.sendServerPacket(user, packetWindowOpen);
			adapter.sendServerPacket(user, packetWindowItems);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
		// handle event
		MenuActionEvent event = new MenuActionEvent(session, EventType.OPEN_MENU);
		menu.handle(event);
	}


	/**
	 * 处理玩家点击菜单事件。
	 * @param event
	 */
	public static void handleEvent(PacketMenuClickEvent event) {
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		IconActionEvent clickEvent = event.getEvent();
		menu.handle(clickEvent);
	}

	/**
	 * 处理玩家关闭菜单事件. 无论玩家是否主动关闭菜单, 都要调用这个方法进行处理.
	 * @param event
	 */
	public static void handleEvent(PacketMenuCloseEvent event) {
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		MenuActionEvent quitEvent = new MenuActionEvent(session, EventType.OPEN_MENU);
		menu.handle(quitEvent);
		session.getUser().setCurrentSession(null);
	}

}
