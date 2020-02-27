package com.blzeecraft.virtualmenu.core.action.event;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.user.UserSession;

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
	

	public IconActionEvent(UserSession session, ClickType type, int rawSlot, int slot,
			AbstractItem<?> current) {
		super(session);
		this.type = type;
		this.rawSlot = rawSlot;
		this.slot = slot;
		this.current = current;
	}

	@Override
	public ClickType getClickType() {
		return type;
	}
	
	
	
	
	
	

}
