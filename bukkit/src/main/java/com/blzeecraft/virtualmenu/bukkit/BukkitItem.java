package com.blzeecraft.virtualmenu.bukkit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.item.BukkitItemBuilder;
import com.blzeecraft.virtualmenu.bukkit.item.ItemUtils;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

public class BukkitItem extends AbstractItem<ItemStack> {
	public static final Set<String> EMPTY_TYPE = new HashSet<>(
			Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR", "LEGACY_AIR"));
	public static final BukkitItem EMPTY_ITEM = new BukkitItem(new ItemStack(Material.AIR), "");
	protected final ItemStack item;

	public BukkitItem(ItemStack item, String nbt) {
		super(item, item.getType().name(), item.getAmount(), ItemUtils.getDisplayname(item), ItemUtils.getLore(item),
				nbt);
		this.item = item;
	}

	public BukkitItem(ItemStack item) {
		super(item, item.getType().name(), item.getAmount(), ItemUtils.getDisplayname(item), ItemUtils.getLore(item));
		this.item = item;
	}

	@Override
	public BukkitItemBuilder builder() {
		return new BukkitItemBuilder(this);
	}

	@Override
	public boolean isEmpty() {
		return EMPTY_TYPE.contains(item.getType().name());
	}

	@Override
	public ItemStack getHandle() {
		return item.clone();
	}

}
