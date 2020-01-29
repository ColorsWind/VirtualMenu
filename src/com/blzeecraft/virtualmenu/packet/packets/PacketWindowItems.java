package com.blzeecraft.virtualmenu.packet.packets;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketWindowItems extends Packet {
	public static final boolean arrayItem;

	
	static {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
		arrayItem = packet.getItemArrayModifier().size() >= 1;
	}
	

	public PacketWindowItems(int windowId, ItemStack[] items) {
		super(new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS));
		int length = items.length;
		for(int i=0;i<length;i++) {
			if (items[i] == null) {
				items[i] = new ItemStack(Material.AIR, 0);
			} 
		}
		packet.getIntegers().write(0, windowId);
		if (arrayItem) {
			packet.getItemArrayModifier().write(0, items);
		} else {
			packet.getItemListModifier().write(0, Arrays.asList(items));
		}

	}
	

}
