package com.blzeecraft.virtualmenu.core.module;

import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketWindowClick;
import lombok.val;

public class PacketHandler {

	public void handle(AbstractPacketCloseWindow<?> packet) {
		IPacketMenu menu = PacketManager.instance.openMenus.remove(packet.getUser());
		if (menu != null) {
			menu.removeViewer(packet.getUser());
		}
	}

	public void handle(AbstractPacketWindowClick<?> packet) {
		val menu = PacketManager.instance.openMenus.remove(packet.getUser());
		if (menu == null) {
			return;
		}
		val windowId = menu.getWindowId();
		val user = packet.getUser();
		val rawSlot = packet.getRawSlot();
		val slot = getSlot(rawSlot, menu.getSize());
		val type = packet.getClickType();
		val event = new ClickEvent(user, type,
				slot, rawSlot, packet.getClickedItem());
		if ((event.isCancel() || slot == packet.getRawSlot()) && !type.isBorderClick()) { 
			packet.setCancel(true);
			//下面开始考虑重设背包显示
			if (menu.isStoreOnly()) {
				val setSlot = VirtualMenu.createPacketSetSlot(user, packet.getWindowId(), slot, menu.viewItem(user, slot));
				setSlot.send();
				if (type.isShiftClick() || type.isKeyboardClick()) {
					user.updateInventory();
				}
			} else {
				val windowItems = VirtualMenu.createPacketWindowItems(user, windowId, menu.viewItems(user));
				windowItems.send();
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
