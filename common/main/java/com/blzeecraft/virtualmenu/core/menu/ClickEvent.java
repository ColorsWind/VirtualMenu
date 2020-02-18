package com.blzeecraft.virtualmenu.core.menu;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.Data;

/**
 * 代表一个玩家点击菜单中的物品的事件
 * @author colors_wind
 *
 */
@Data
public class ClickEvent {
	
	
	private final IUser<?> user;
	private final ClickType type;
	private final int rawSlot;
	private final int slot;
	private final AbstractItem<?> current;
	/**
	 * 仅当玩家点击玩家背包的菜单这个选项才有效
	 * 如果该选项为true，将会阻止玩家移动背包的物品
	 */
	private boolean cancel = true;
	
	
	
	

}
