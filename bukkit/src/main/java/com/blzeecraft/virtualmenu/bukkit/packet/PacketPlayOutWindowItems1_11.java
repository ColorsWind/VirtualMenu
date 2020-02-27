package com.blzeecraft.virtualmenu.bukkit.packet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.WrapItemBukkit;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutWindowItems1_11 extends AbstractPacketOutWindowItems<PacketContainer> {
	public static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayOutWindowItems1_11() {
		this(new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS));
	}

	public PacketPlayOutWindowItems1_11(PacketContainer packet) {
		super(packet);
	}

	@Override
	public void setItems(AbstractItem<?>[] items) {
		List<ItemStack> itemList = Arrays.stream(items).map(item -> item == null ? EMPTY_ITEM : (ItemStack)item.getHandle()).collect(Collectors.toList());
		packet.getItemListModifier().write(0, itemList);
	}
	

	@Override
	public AbstractItem<?>[] getItems() {
		List<ItemStack> itemList = packet.getItemListModifier().read(0);
		return itemList.stream().map(WrapItemBukkit::new).collect(Collectors.toList())
				.toArray(new WrapItemBukkit[itemList.size()]);
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
		packet.getItemListModifier().write(0, Arrays.asList(BukkitPlatform.castItemArray(items)));
	}

	@Override
	public ItemStack[] getRawItems() {
		List<ItemStack> items = packet.getItemListModifier().read(0);
		return items.toArray(new ItemStack[items.size()]);
	}

}
