package com.blzeecraft.virtualmenu.core.packet;

import java.lang.reflect.InvocationTargetException;

import com.blzeecraft.virtualmenu.core.ThreadSafe;
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
import com.blzeecraft.virtualmenu.core.user.UserManager;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.val;

/**
 * 用于处理发送/接收的 Packet. 
 * @author colors_wind
 *
 */
@ThreadSafe
public class PacketHandler {
	public static final LogNode LOG_NODE = LogNode.of("#PacketHandler");
	
	@ThreadSafe
	public static boolean handleServerPacket(UserSession session, AbstractPacketOutWindowItems<?> packet) {
		session.handlePacket(packet);
		return false;
	}
	
	@ThreadSafe
	public static boolean handleServerPacket(UserSession session, AbstractPacketOutSetSlot<?> packet) {
		session.handlePacket(packet);
		return false;
	}
	
	@ThreadSafe
	public static boolean handleClientPacket(UserSession session, AbstractPacketInCloseWindow<?> packet) {
		val menu = session.getMenu();
		if (menu.getWindowId() == packet.getWindowId()) {
			VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> UserManager.handleClosePacketMenu(session));
		}
		return false;
		
	}
	
	@ThreadSafe
	public static boolean handleClientPacket(UserSession session, AbstractPacketInWindowClick<?> packet) {
		val menu = session.getMenu();
		if (menu.getWindowId() == packet.getWindowId()) {
			ClickType clickType = packet.getClickType();
			int size = menu.getSize();
			int rawSlot = packet.getRawSlot();
			int slot = rawSlot < size ? rawSlot : rawSlot - size;
			AbstractItem<?> clickedItem = packet.getClickedItem();
			resetPlayerView(session, menu, clickType, rawSlot);
			switch(clickType) {
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
				IconActionEvent clickEvent = new IconActionEvent(session, clickType, rawSlot, slot, clickedItem);
				VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> {
					menu.handle(clickEvent);
				});
				break;
			}
			return true;
		}
		return false;
	}
	
	@ThreadSafe
	private static void resetPlayerView(UserSession session, IPacketMenu menu, ClickType type, int rawSlot) {
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		IUser<?> user = session.getUser();
		switch(type) {
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
			AbstractPacketOutSetSlot<?> click_resetHold = session.createResetHoldPacket();
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
			AbstractPacketOutSetSlot<?> full_resetHold = session.createResetHoldPacket();
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

	

}
