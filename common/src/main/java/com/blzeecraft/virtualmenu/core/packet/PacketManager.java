package com.blzeecraft.virtualmenu.core.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.val;

/**
 * 处理有关 Packet 和 UserSession 的逻辑.
 * 说明这里一些有可能修改玩家当前状态的操作(检查条件, 执行动作等)都会在主线程进行.
 * 所有不涉及修改玩家当前状态的操作(用于重设玩家背包显示上的物品, 发送Packet等)都会在当前线程进行.
 * 值得注意的是, 这种策略理论上并是安全, 例如读取玩家背包上的
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
public class PacketManager {
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
		// handle close event first
		PacketMenuCloseEvent event = new PacketMenuCloseEvent(session, false);
		handleEvent(event);
	}
	
	public static void closePacketMenu(IUser<?> user) {
		user.getCurrentSession().ifPresent(PacketManager::closePacketMenu);
	}
	
	public static Optional<PacketMenuCloseEvent> map(UserSession session, AbstractPacketInCloseWindow<?> packet) {
		if (session.getMenu().getWindowId() == packet.getWindowId()) {
			return Optional.of(new PacketMenuCloseEvent(session, false));
		}
		return Optional.empty();
		
	}
	public static Optional<PacketMenuClickEvent> map(UserSession session, AbstractPacketInWindowClick<?> packet) { 
		IPacketMenu menu = session.getMenu();
		if (menu.getWindowId() == packet.getWindowId()) {
			ClickType type = packet.getClickType();
			int size = menu.getSize();
			int rawSlot = packet.getRawSlot();
			int slot = rawSlot < size ? rawSlot : rawSlot - size;
			AbstractItem<?> clickedItem = packet.getClickedItem();
			PacketMenuClickEvent event = new PacketMenuClickEvent(session, new IconActionEvent(session, type, rawSlot, slot, clickedItem));
			return Optional.of(event);
		}
		return Optional.empty();
	}
	
	private static void sendResetPacket(PacketMenuClickEvent event) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		UserSession session = event.getSession();
		IUser<?> user = session.getUser();
		IconActionEvent iconEvent = event.getIconEvent();
		int rawSlot = iconEvent.getRawSlot();
		switch(iconEvent.getClickType()) {
		case CONTROL_DROP:
		case DROP:
			// update click slot
			AbstractPacketOutSetSlot<?> drop_resetClick = session.createPacketOutSetSlotForMenu(rawSlot);
			try {
				adapter.sendServerPacket(user, drop_resetClick);
			} catch (InvocationTargetException e1) {
				PluginLogger.warning(LOG_NODE, "发送 Click Update Packet 发生错误.");
				e1.printStackTrace();
			}
			break;
		case RIGHT:
		case LEFT:
			// update click slot and hold;
			AbstractPacketOutSetSlot<?> click_resetHold = createResetHoldPacket();
			AbstractPacketOutSetSlot<?> click_resetClick = session.createPacketOutSet(rawSlot);
			try {
				adapter.sendServerPacket(user, click_resetHold);
				adapter.sendServerPacket(user, click_resetClick);
			} catch (InvocationTargetException e2) {
				PluginLogger.warning(LOG_NODE, "发送 Click & Slot Update Packet 发生错误.");
				e2.printStackTrace();
			}
			break;
		case MIDDLE:
		case DOUBLE_CLICK:
		case CREATIVE:
		case NUMBER_KEY:
		case SHIFT_LEFT:
		case SHIFT_RIGHT:
		case UNKNOWN:
		default:
			// full update
			AbstractPacketOutSetSlot<?> full_resetHold = createResetHoldPacket();
			AbstractPacketOutWindowItems<?> full_resetInv = session.createPacketWindowItemsForInventory();
			AbstractPacketOutWindowItems<?> full_resetMenu = session.createPacketWindowItemsForMenu();
			try {
				adapter.sendServerPacket(user, full_resetHold);
				adapter.sendServerPacket(user, full_resetInv);
				adapter.sendServerPacket(user, full_resetMenu);
			} catch (InvocationTargetException e3) {
				PluginLogger.warning(LOG_NODE, "发送 Full Update Packet 发生错误.");
				e3.printStackTrace();
			}

			break;
		case WINDOW_BORDER_LEFT:
		case WINDOW_BORDER_RIGHT:
			// nope
			break;
		}
	}
	
	private static AbstractPacketOutSetSlot<?> createResetHoldPacket() {
		AbstractPacketOutSetSlot<?> resetHold = VirtualMenu.getPacketAdapter().createPacketSetSlot();
		resetHold.setWindowId(-1);
		resetHold.setSlot(-1);
		resetHold.setItem(VirtualMenu.emptyItem());
		return resetHold;
	}
	
	
	

	/**
	 * 处理玩家关闭菜单事件. 无论玩家是否主动关闭菜单, 都要调用这个方法进行处理.
	 * 
	 * @param event
	 */	
	public static void handleEvent(PacketMenuCloseEvent event) {
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		MenuActionEvent closeEvent = new MenuActionEvent(session, EventType.CLOSE_MENU);
		VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
			menu.handle(closeEvent);
			//ensure sequential execution
			session.getUser().setCurrentSession(null);
		});
	}
	
	/**
	 * 处理玩家点击菜单事件。
	 * 
	 * @param event
	 */
	public static void handleEvent(PacketMenuClickEvent event) {
		sendResetPacket(event);
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		switch(event.iconEvent.getClickType()) {
		case WINDOW_BORDER_LEFT:
			VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
				menu.handle(new MenuActionEvent(session, EventType.LEFT_BORDER_CLICK));
			});
			break;
		case WINDOW_BORDER_RIGHT:
			VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
				menu.handle(new MenuActionEvent(session, EventType.RIGHT_BORDER_CLICK));
			});
			break;
		default:
			IconActionEvent clickEvent = event.getIconEvent();
			VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
				menu.handle(clickEvent);
			});
			break;
		}

	}
	
	public void handleServerPacket(UserSession session, AbstractPacketOutWindowItems<?> packet) {
		session.handlePacket(packet);
	}
	
	public void handleServerPacket(UserSession session, AbstractPacketOutSetSlot<?> packet) {
		session.handlePacket(packet);
	}

	public static void closeAllMenu() {
		VirtualMenu.getUsersOnline().stream().map(IUser::getCurrentSession).filter(Optional::isPresent)
				.map(Optional::get).forEach(PacketManager::closePacketMenu);
	}
	

}
