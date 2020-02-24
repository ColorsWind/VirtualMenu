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
			adapter.sendServerPacket(user, packetWindowClose);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送关闭背包 Packet 时发生异常.");
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
			adapter.sendServerPacket(session.getUser(), packetWindowClose);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送关闭菜单 Packet 时发生异常.");
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
		packetWindowOpen.setMenuType(menu.getType());
		packetWindowOpen.setTitle(menu.getTitle());
		AbstractPacketOutWindowItems<?> packetWindowItems = adapter.createPacketWindowItems();
		packetWindowItems.setWindowId(menu.getWindowId());
		packetWindowItems.setItems(menu.viewItems(session));
		// send packet
		try {
			adapter.sendServerPacket(user, packetWindowOpen);
			adapter.sendServerPacket(user, packetWindowItems);
		} catch (InvocationTargetException e) {
			PluginLogger.warning(LOG_NODE, "发送打开菜单 Packet 时发生异常.");
			e.printStackTrace();
			return;
		}
		// handle event
		MenuActionEvent event = new MenuActionEvent(session, EventType.OPEN_MENU);
		menu.handle(event);
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
			PacketMenuClickEvent event = new PacketMenuClickEvent(session, new IconActionEvent(session, type, rawSlot, slot, clickedItem, false));
			return Optional.of(event);
		}
		return Optional.empty();
	}
	
	private static void sendResetPacket(PacketMenuClickEvent event) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		UserSession session = event.getSession();
		IUser<?> user = session.getUser();
		IconActionEvent iconEvent = event.getEvent();
		IPacketMenu menu = iconEvent.getMenu();
		switch(event.getEvent().getClickType()) {
		case CONTROL_DROP:
		case DROP:
			// update click slot
			int drop_windowId = iconEvent.getMenu().getWindowId();
			int drop_rawSlot = iconEvent.getRawSlot();
			int drop_slot = iconEvent.getSlot();
			AbstractItem<?> drop_clickedItem = iconEvent.getCurrent();
			AbstractPacketOutSetSlot<?> drop_resetClick= createResetClickPacket(drop_windowId, drop_rawSlot, drop_slot, drop_clickedItem);
			adapter.sendServerPacketWrap(user, drop_resetClick);
			break;
		case RIGHT:
		case LEFT:
			// update click slot and hold;
			int click_windowId = iconEvent.getMenu().getWindowId();
			int click_rawSlot = iconEvent.getRawSlot();
			int click_slot = iconEvent.getSlot();
			AbstractItem<?> click_clickedItem = iconEvent.getCurrent();
			AbstractPacketOutSetSlot<?> click_resetHold = createResetHoldPacket();
			AbstractPacketOutSetSlot<?> click_resetClick = createResetClickPacket(click_windowId, click_rawSlot, click_slot, click_clickedItem);
			adapter.sendServerPacketWrap(user, click_resetHold, click_resetClick);
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
			int full_windowId = menu.getWindowId();
			AbstractPacketOutSetSlot<?> full_resetHold = createResetHoldPacket();
			AbstractPacketOutWindowItems<?> full_resetMenu = adapter.createPacketWindowItems();
			full_resetMenu.setWindowId(full_windowId);
			full_resetMenu.setItems(menu.viewItems(event.getSession()));
			VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
				adapter.sendServerPacketWrap(user, full_resetMenu, full_resetHold);
				user.updateInventory();
			});
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
	
	/**
	 * 处理玩家点击菜单事件。
	 * 
	 * @param event
	 */
	public static void handleEvent(PacketMenuClickEvent event) {
		sendResetPacket(event);
		UserSession session = event.getSession();
		IPacketMenu menu = session.getMenu();
		switch(event.event.getClickType()) {
		case WINDOW_BORDER_LEFT:
			menu.handle(new MenuActionEvent(session, EventType.LEFT_BORDER_CLICK));
			break;
		case WINDOW_BORDER_RIGHT:
			menu.handle(new MenuActionEvent(session, EventType.RIGHT_BORDER_CLICK));
			break;
		default:
			IconActionEvent clickEvent = event.getEvent();
			menu.handle(clickEvent);
			break;
		}

	}

	public static void closeAllMenu() {
		VirtualMenu.getUsersOnline().stream().map(IUser::getCurrentSession).filter(Optional::isPresent)
				.map(Optional::get).forEach(PacketManager::closePacketMenu);
	}
	

}
