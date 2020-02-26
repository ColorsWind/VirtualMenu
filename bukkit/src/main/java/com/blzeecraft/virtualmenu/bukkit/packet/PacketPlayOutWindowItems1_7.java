package com.blzeecraft.virtualmenu.bukkit.packet;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.WrapItemBukkit;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutWindowItems1_7 extends AbstractPacketOutWindowItems<PacketContainer> {
	public static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayOutWindowItems1_7() {
		super(new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS));
	}

	@Override
	public void setItems(AbstractItem<?>[] items) {
		int size = items.length;
		ItemStack[] itemArray = new ItemStack[size];
		for (int i = 0; i < size; i++) {
			itemArray[i] = items[i] == null ? EMPTY_ITEM : (ItemStack) items[i].getHandle();
		}
		packet.getItemArrayModifier().write(0, itemArray);
	}

	@Override
	public AbstractItem<?>[] getItems() {
		ItemStack[] itemArray = getRawItems();
		return Arrays.stream(itemArray).map(WrapItemBukkit::new).collect(Collectors.toList())
				.toArray(new WrapItemBukkit[itemArray.length]);
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getIntegers().write(0, windowId);
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

	@Override
	public void setRawItems(Object[] items) {
		packet.getItemArrayModifier().write(0, BukkitPlatform.castItemArray(items));
	}

	@Override
	public ItemStack[] getRawItems() {
		return packet.getItemArrayModifier().read(0);
	}

}
