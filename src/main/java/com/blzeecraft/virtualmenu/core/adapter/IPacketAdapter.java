package com.blzeecraft.virtualmenu.core.adapter;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.module.PacketHandler;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketSetSlot;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketWindowItems;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketWindowOpen;


public interface IPacketAdapter {
	

	AbstractPacketCloseWindow<?> createPacketCloseWindow(IUser<?> user, int windowId);

	AbstractPacketWindowOpen<?> createPacketWindOpen(IUser<?> user, int windowId, IMenuType type, String title);

	AbstractPacketSetSlot<?> createPacketSetSlot(IUser<?> user, int windowId, int slot, AbstractItem<?> item);

	AbstractPacketWindowItems<?> createPacketWindowItems(IUser<?> user, int windowId, AbstractItem<?>[] items);

	boolean registerPacketHandler(PacketHandler handler);
}
