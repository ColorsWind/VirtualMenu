package com.blzeecraft.virtualmenu.bukkit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.bukkit.item.BukkitItemBuilder;
import com.blzeecraft.virtualmenu.bukkit.item.ItemUtils;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.ToString;

@ToString
public class WrapItemBukkit extends AbstractItem<ItemStack> {
	public static final Set<String> EMPTY_TYPE = new HashSet<>(
			Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR", "LEGACY_AIR"));
	public static final WrapItemBukkit EMPTY_ITEM = new WrapItemBukkit(new ItemStack(Material.AIR), "");
	protected final ItemStack item;

	public WrapItemBukkit(ItemStack item, String nbt) {
		super(item, item.getType().name(), item.getAmount(), ItemUtils.getDisplayname(item), ItemUtils.getLore(item),
				nbt);
		this.item = item;
	}

	public WrapItemBukkit(ItemStack item) {
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

	@Override
	public WrapItemBukkit updateMeta(String newName, List<String> newLore) {
		ItemStack newItem = this.item.clone();
		ItemMeta meta = newItem.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(newName);
			meta.setLore(newLore);
			newItem.setItemMeta(meta);
		}
		return new WrapItemBukkit(newItem);
	}

}
