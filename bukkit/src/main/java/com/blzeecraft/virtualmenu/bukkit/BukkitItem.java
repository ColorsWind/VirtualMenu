package com.blzeecraft.virtualmenu.bukkit;

import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.item.BukkitItemBuilder;
import com.blzeecraft.virtualmenu.bukkit.item.ItemUtils;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

public class BukkitItem extends AbstractItem<ItemStack> {
	protected final ItemStack item;

	public BukkitItem(ItemStack item, String nbt) {
		super(item, item.getType().name(), item.getAmount(), ItemUtils.getDisplayname(item), ItemUtils.getLore(item), nbt);
		this.item = item;
	}

	@Override
	public BukkitItemBuilder builder() {
		return new BukkitItemBuilder(this);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
