package com.blzeecraft.virtualmenu.core;

import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author colors_wind
 * @date 2020-02-10
 */
@AllArgsConstructor
@ToString
@Getter
public abstract class MenuEvent {
	
	protected final IUser<?> user;
	protected final IPacketMenu menu;
	
	
	public ClickType getClickType() {
		return ClickType.UNKNOWN;
	}

}
