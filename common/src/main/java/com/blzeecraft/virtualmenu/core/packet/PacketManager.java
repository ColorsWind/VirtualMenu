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

	public static void closePacketMenu(UserSession session) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		// handle close event first
		PacketMenuCloseEvent event = new PacketMenuCloseEvent(session, false);
		handleEvent(event);
		// create and send packet
		AbstractPacketOutCloseWindow<?> packetWindowClose = adapter.createPacketCloseWindow();
		packetWindowClose.setWindowId(session.getMenu().getWindowId());
		try {
			PluginLogger.warning(LOG_NODE, "发送关闭菜单 Packet 时发送异常.");
			adapter.sendServerPacket(session.getUser(), packetWindowClose);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static void closePacketMenu(IUser<?> user) {
		user.getCurrentSession().ifPresent(PacketManager::closePacketMenu);
	}

	public static void openMenuUncheck(IUser<?> user, IPacketMenu menu) {
		closePacketMenu(user); // ensure packet-menu are closed.
		closeInventory(user); // ensure inventory are closed.
		UserSession session = new UserSession(user, menu);
		menu.addViewer(session);
		user.setCurrentSession(session);
		// create packet
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		AbstractPacketOutWindowOpen<?> packetWindowOpen = adapter.createPacketWindowOpen();
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
	 * 
	 * @param event
	 */
	public static void handleEvent(PacketMenuClickEvent event) {
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		IconActionEvent clickEvent = event.getEvent();
		menu.handle(clickEvent);
	}
	
	public static Optional<PacketMenuCloseEvent> map(UserSession session, AbstractPacketInCloseWindow<?> packet) {
		if (session.getMenu().getWindowId() == packet.getWindowId()) {
			return Optional.of(new PacketMenuCloseEvent(session, false));
		}
		return Optional.empty();
		
	}
	
	public static Optional<PacketMenuClickEvent> map(UserSession session, AbstractPacketInWindowClick<?> packet) {
		IPacketMenu menu = session.getMenu();
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		IUser<?> user = session.getUser();
		if (menu.getWindowId() == packet.getWindowId()) {
			ClickType type = packet.getClickType();
			int windowId = menu.getWindowId();
			int size = menu.getSize();
			int rawSlot = packet.getRawSlot();
			AbstractItem<?> clickedItem = packet.getClickedItem();
			switch(type) {
			case CONTROL_DROP:
			case DROP:
				AbstractPacketOutSetSlot<?> drop_resetHold = createResetHoldPacket();
				adapter.sendServerPacketWrap(user, drop_resetHold);
				break;
			case RIGHT:
			case LEFT:
				AbstractPacketOutSetSlot<?> click_resetHold = createResetHoldPacket();
				AbstractPacketOutSetSlot<?> click_resetClick = createResetClickPacket(windowId, rawSlot, size, clickedItem);
				VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
					adapter.sendServerPacketWrap(user, click_resetHold, click_resetClick);
				});
				break;
			case MIDDLE:
			case DOUBLE_CLICK:
			case CREATIVE:
			case NUMBER_KEY:
			case SHIFT_LEFT:
			case SHIFT_RIGHT:
			case UNKNOWN:
			default:
				//full update


				break;
			case WINDOW_BORDER_LEFT:
				break;
			case WINDOW_BORDER_RIGHT:
				break;

				break;
			
			}
			if (menu.getSize() > packet.getRawSlot()) {
				//click packet menu
				
				
			}

			//packet.getClickMode()
		}
		return Optional.empty();
		
	}
	
	private static AbstractPacketOutSetSlot<?> createResetHoldPacket() {
		AbstractPacketOutSetSlot<?> resetHold = VirtualMenu.getPacketAdapter().createPacketSetSlot();
		resetHold.setWindowId(-1);
		resetHold.setSlot(-1);
		resetHold.setItem(VirtualMenu.emptyItem());
		return resetHold;
	}
	
	private static AbstractPacketOutSetSlot<?> createResetClickPacket(int windowId, int rawSlot, int size, AbstractItem<?> clickedItem) {
		AbstractPacketOutSetSlot<?> resetClick = VirtualMenu.getPacketAdapter().createPacketSetSlot();
		if (size > rawSlot) {
			resetClick.setWindowId(windowId);
			resetClick.setWindowId(rawSlot);
			resetClick.setItem(clickedItem);
		} else {
			int slot = rawSlot - size;
			resetClick.setWindowId(-1);
			resetClick.setWindowId(slot);
			resetClick.setItem(clickedItem);
		}
		return resetClick;
	}

	/**
	 * 处理玩家关闭菜单事件. 无论玩家是否主动关闭菜单, 都要调用这个方法进行处理.
	 * 
	 * @param event
	 */
	public static void handleEvent(PacketMenuCloseEvent event) {
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		MenuActionEvent quitEvent = new MenuActionEvent(session, EventType.OPEN_MENU);
		menu.handle(quitEvent);
		session.getUser().setCurrentSession(null);
	}

	public static void closeAllMenu() {
		VirtualMenu.getUsersOnline().stream().map(IUser::getCurrentSession).filter(Optional::isPresent)
				.map(Optional::get).forEach(PacketManager::closePacketMenu);
	}
	

}
