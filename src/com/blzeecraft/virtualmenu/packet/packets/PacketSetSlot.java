package com.blzeecraft.virtualmenu.packet.packets;

import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketSetSlot extends Packet {

	public PacketSetSlot(Integer windowId, int slot, ItemStack item) {
		super(new PacketContainer(PacketType.Play.Server.SET_SLOT));
		packet.getIntegers().write(0, windowId);
		packet.getIntegers().write(1, slot);
		packet.getItemModifier().write(0, item);
	}

}
