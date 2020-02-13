package com.blzeecraft.virtualmenu.core.menu;

import com.blzeecraft.virtualmenu.core.MenuEvent;
import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.Getter;
import lombok.ToString;


/**
 * 代表一个玩家点击菜单中的物品的事件
 * @author colors_wind
 *
 */
@Getter
@ToString(callSuper=true)
public class IconActionEvent extends MenuEvent {
	
	protected final ClickType type;
	protected final int rawSlot;
	protected final int slot;
	protected final AbstractItem<?> current;
	
	/**
	 * 仅当玩家点击玩家背包的菜单这个选项才有效
	 * 如果该选项为true，将会阻止玩家移动背包的物品
	 */
	private boolean cancel = true;

	public IconActionEvent(UserSession session, ClickType type, int rawSlot, int slot,
			AbstractItem<?> current, boolean cancel) {
		super(session);
		this.type = type;
		this.rawSlot = rawSlot;
		this.slot = slot;
		this.current = current;
		this.cancel = cancel;
	}

	@Override
	public ClickType getClickType() {
		return type;
	}
	
	
	
	
	
	

}
