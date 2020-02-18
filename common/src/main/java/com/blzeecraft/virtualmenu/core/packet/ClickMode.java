package com.blzeecraft.virtualmenu.core.packet;

/**
 * 玩家点击菜单中物品的方式, 该枚举仅用于解析数据包.
 * @author colors_wind
 *
 */
public enum ClickMode {
	PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL;
}

