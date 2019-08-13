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
	
	public int getId(int slot) {
		if (bukkit == InventoryType.CHEST) {
			switch(slot) {
			case 9:
				return 0;
			case 18:
				return 1;
			case 27:
				return 2;
			case 36:
				return 3;
			case 45:
				return 4;
			case 54:
			default:
				return 5;
			}
		} else {
			throw new UnsupportedOperationException("暂时不支持箱子菜单以外的类型");
		}
	}
}
