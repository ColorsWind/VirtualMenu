package com.blzeecraft.virtualmenu.builder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.Data;

@Data
public class MenuHolder implements InventoryHolder {
	private final String name;
	private final String title;

	@Override
	public Inventory getInventory() {
		return null;
	}

	public MenuHolder() {
		this(null, null);
	}

	public MenuHolder(String name, String title) {
		super();
		this.name = name;
		this.title = title;

	}
	
	

}
