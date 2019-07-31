package com.blzeecraft.virtualmenu.packet;

import org.bukkit.event.inventory.InventoryType;

import lombok.Getter;

@Getter
public enum EnumInventoryType {
	ENDER_CHEST("minecraft:container", InventoryType.ENDER_CHEST), 
	CHEST("minecraft:chest", InventoryType.CHEST),
	ANVIL("minecraft:anvil", InventoryType.ANVIL),
	HOPPER("minecraft:hopper", InventoryType.HOPPER);
	private final String minecraft;
	private final InventoryType bukkit;

	EnumInventoryType(String minecraft, InventoryType bukkit) {
		this.minecraft = minecraft;
		this.bukkit = bukkit;
	}
	
	public String minecraft() {
		return this.minecraft;
	}
}
