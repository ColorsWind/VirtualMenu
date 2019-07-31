package com.blzeecraft.virtualmenu.packet.packets;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketWindowItems extends Packet {

	public PacketWindowItems(int windowId, ItemStack[] items) {
		super(new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS));
		int length = items.length;
		for(int i=0;i<length;i++) {
			if (items[i] == null) {
				items[i] = new ItemStack(Material.AIR, 0);
			} 
		}
		List<ItemStack> list = Arrays.asList(items);
		packet.getIntegers().write(0, windowId);
		packet.getItemListModifier().write(0, list);
	}
	

}
