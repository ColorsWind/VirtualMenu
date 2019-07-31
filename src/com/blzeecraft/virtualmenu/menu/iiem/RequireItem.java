package com.blzeecraft.virtualmenu.menu.iiem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class RequireItem extends Item {
	
	protected ILog parent;
	
	public RequireItem(ILog parent) {
		this.parent = parent;
	}
	
	@Getter
	public class Result {
		private final Inventory inv;
		private final Map<Integer, ItemStack> modify;
		private final int rest;
		
		
		public Result(Inventory inv, Map<Integer, ItemStack> modify, int rest) {
			this.inv = inv;
			this.modify = modify;
			this.rest = rest;
		}
		
		public boolean hasItem() {
			return rest == 0;
		}
		
		public void take() {
			for(Entry<Integer, ItemStack> en : modify.entrySet()) {
				inv.setItem(en.getKey(), en.getValue());
			}
		}
	}
	
	
	
	public Result take(Inventory inv) {
		int total = this.amount;
		ItemStack[] contents = inv.getContents();
		HashMap<Integer, ItemStack> modify = new HashMap<>();
		for(int i=0;i<contents.length;i++) {
			ItemStack item = contents[i];
			if (match(item)) {
				int amount = item.getAmount();
				int newAmount = amount - total;
				if (newAmount < 0) {
					item.setAmount(0);
					item.setType(Material.AIR);
					total -= amount;
					modify.put(i, item);
				} else if (newAmount == 0){
					item.setAmount(0);
					item.setType(Material.AIR);
					total = 0;
					modify.put(i, item);
					return new Result(inv, modify, 0);
				} else {
					item.setAmount(0);
					total = 0;
					return new Result(inv, modify, 0);
				}
			}
		}
		return new Result(inv, null, total);
	}

	@SuppressWarnings("deprecation")
	public boolean match(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (cacheItem.getType() != item.getType()) {
			return false;
		}
		if (cacheItem.getDurability() != item.getDurability()) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		if (this.displayname != null && (meta == null || !this.displayname.equals(meta.getDisplayName()))) {
			return false;
		}
		if (this.lore != null && (meta == null || !this.lore.equals(meta.getLore()))) {
			return false;
		}
		return true;
	}

	@Override
	public String getLogPrefix() {
		return ILog.sub(super.getLogPrefix(), "REQUIRED-ITEM");
	}


}
