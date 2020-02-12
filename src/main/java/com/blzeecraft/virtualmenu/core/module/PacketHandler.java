package com.blzeecraft.virtualmenu.core.module;

import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInWindowClick;

import lombok.val;

/**
 * 数据包监听器
 * @author colors_wind
 *
 */
public class PacketHandler {

	public void handle(AbstractPacketInCloseWindow<?> packet) {
		IPacketMenu menu = PacketManager.INSTANCE.openMenus.remove(packet.getUser());
		if (menu != null) {
			menu.removeViewer(packet.getUser());
		}
	}

	public void handle(AbstractPacketInWindowClick<?> packet) {
		val menu = PacketManager.INSTANCE.openMenus.remove(packet.getUser());
		if (menu == null) {
			return;
		}
		val user = packet.getUser();
		val rawSlot = packet.getRawSlot();
		val slot = getSlot(rawSlot, menu.getSize());
		val type = packet.getClickType();
		val event = new IconActionEvent(user, type,
				slot, rawSlot, packet.getClickedItem());
		if ((event.isCancel() || slot == packet.getRawSlot()) && !type.isBorderClick()) { 
			packet.setCancel(true);
			//下面开始考虑重设背包显示
			if (menu.totalUpdate()) {
				menu.update(user, slot);
				if (type.isShiftClick() || type.isKeyboardClick()) {
					user.updateInventory();
				}
			} else {
				menu.update(user);
				user.updateInventory();
			}
			
		}
	}

	public int getSlot(int rawSlot, int invSize) {
		int slot = rawSlot - invSize;
		if (slot < 0) {
			return rawSlot;
		}
		return slot;
	}
}
