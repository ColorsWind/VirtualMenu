package com.blzeecraft.virtualmenu.bukkit.packet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.BukkitItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutWindowItems1_11 extends AbstractPacketOutWindowItems<PacketContainer> {
	public static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayOutWindowItems1_11() {
		super(new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS));
	}

	@Override
	public void setItems(AbstractItem<?>[] items) {
		List<ItemStack> itemList = Arrays.stream(items).map(item -> item == null ? EMPTY_ITEM : (ItemStack)item.getHandle()).collect(Collectors.toList());
		packet.getItemListModifier().write(0, itemList);
	}

	@Override
	public AbstractItem<?>[] getItems() {
		List<ItemStack> itemList = packet.getItemListModifier().read(0);
		return itemList.stream().map(BukkitItem::new).collect(Collectors.toList())
				.toArray(new BukkitItem[itemList.size()]);
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getIntegers().write(0, windowId);
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

}
