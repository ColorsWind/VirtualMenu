package com.blzeecraft.virtualmenu.bukkit;

import org.bukkit.event.inventory.InventoryType;

import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import lombok.ToString;

@ToString
public enum MenuType implements IMenuType {

	// 箱子等
	GENERIC_9X1(0, 0, "minecraft:chest", 9),
	GENERIC_9X2(1, 0, "minecraft:chest", 18),
	GENERIC_9X3(2, 0, "minecraft:chest", 27),
	GENERIC_9X4(3, 0, "minecraft:chest", 36),
	GENERIC_9X5(4, 0, "minecraft:chest", 45),
	GENERIC_9X6(5, 0, "minecraft:chest", 54),
	// 发射器等
	GENERIC_3X3(6, 3, "minecraft:dispenser", 9),
	// 铁毡
	ANVIL(7, 8, "minecraft:anvil"),
	// 信标
	BEACON(8, 7, "minecraft:beacon", 1),
	// 高炉
	BLAST_FURNACE(9, "minecraft:blast_furnace", 3),
	// 酿造台
	BREWING_STAND(10, 5, "minecraft:brewing_stand"),
	// 合成台
	CRAFTING(11, 1, "minecraft:crafting_table"),
	// 附魔台
	ENCHANTMENT(12, 4,"minecraft:enchanting_table"),
	// 熔炉
	FURNACE(13, 3, "minecraft:furnace", 2),
	// 砂轮
	GRINDSTONE(14, "minecraft:grindstone", 3),
	// 漏斗
	HOPPER(15, "minecraft:hopper", 5), 
	// 讲台
	LECTERN(16, "minecraft:lectern"),
	// 织布机
	LOOM(17, "minecraft:loom", 4),
	// 村民交易
	MERCHANT(18, 6, "minecraft:merchant"),
	// 潜影盒
	SHULKER_BOX(19, "minecraft:shulker_box", 27),
	// 烟熏炉
	SMOKER(20, "minecraft:smoker", 3),
	// 制图台
	CARTOGRAPHY(21, "minecraft:cartography", 3),
	// 切石机
	STONECUTTER(22, "minecraft:stonecutter");
	private final int index;
	private final int legacyIndex;
	private final String minecraft;
	private final int slot;

	/**
	 * 菜单包含了非物品按钮,如附魔台
	 * 菜单包含了物品的转换,如合成台,铁毡
	 * @param index
	 * @param minecraft
	 */
	private MenuType(int index, String minecraft) {
		this(index, minecraft, -1);
	}

	/**
	 * 菜单包含了非物品按钮,如附魔台
	 * 菜单包含了物品的转换,如合成台,铁毡
	 * @param index
	 * @param legacyIndex
	 * @param minecraft
	 */
	private MenuType(int index, int legacyIndex, String minecraft) {
		this(index, legacyIndex, minecraft, -1);
	}
	
	/**
	 * 菜单是纯物品,如箱子,潜影盒
	 * @param index
	 * @param minecraft
	 * @param slot
	 */
	private MenuType(int index, String minecraft, int slot) {
		this(index, -1, minecraft, slot);
	}
	
	/**
	 * 菜单是纯物品,如箱子,潜影盒
	 * @param index
	 * @param legacyIndex
	 * @param minecraft
	 * @param slot
	 */
	private MenuType(int index, int legacyIndex, String minecraft, int slot) {
		this.index = index;
		this.legacyIndex = legacyIndex;
		this.minecraft = minecraft;
		this.slot = slot;
	}

	public static MenuType fromSlot(int slot) {
		if (slot >= 54) {
			return GENERIC_9X6;
		} else if (slot >= 45) {
			return GENERIC_9X5;
		} else if (slot >= 36) {
			return GENERIC_9X4;
		} else if (slot >= 27) {
			return GENERIC_9X3;
		} else if (slot >= 18) {
			return GENERIC_9X2;
		} else if (slot >= 9) {
			return GENERIC_9X1;
		}
		return GENERIC_9X6;
	}

	public static MenuType fromRow(int row) {
		switch (row) {
		case 1:
			return GENERIC_9X1;
		case 2:
			return GENERIC_9X2;
		case 3:
			return GENERIC_9X3;
		case 4:
			return GENERIC_9X4;
		case 5:
			return GENERIC_9X5;
		case 6:
		default:
			return GENERIC_9X6;
		}
	}


	/**
	 * 获取菜单对应的  {@link InventoryType}.
	 * @return 菜单类型对应的 {@link InventoryType}
	 * @throws IllegalArgumentException 当前版本不支持菜单类型类型时.
	 */
	public InventoryType getBukkitType() throws IllegalArgumentException {
		switch(this) {
		case GENERIC_9X1:
		case GENERIC_9X2:
		case GENERIC_9X3:
		case GENERIC_9X4:
		case GENERIC_9X5:
		case GENERIC_9X6:
			return InventoryType.CHEST;
		case GENERIC_3X3:
			return InventoryType.DISPENSER;
		default:
			try {
				return InventoryType.valueOf(this.name());
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("找不到 InventoryType." + this.name() + ", 可能是服务的版本过低导致的.");
			}
		}

	}
	
	/**
	 * 获取菜单的大小
	 * 该方法会返回一个有效的值
	 * @return 菜单的大小
	 * @throws IllegalArgumentException 当前版本不支持菜单类型类型时
	 * @see #getSlot()
	 */
	public int getSize() throws IllegalArgumentException {
		if (slot == -1) {
			return getBukkitType().getDefaultSize();
		}
		return slot;
	}

	
	public boolean isItemMenu() {
		return slot != -1;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public int getLegacyIndex() {
		return legacyIndex;
	}

	@Override
	public String getType() {
		return this.name();
	}
	
	@Override
	public String getMinecraftKey() {
		return this.minecraft;
	}

}
