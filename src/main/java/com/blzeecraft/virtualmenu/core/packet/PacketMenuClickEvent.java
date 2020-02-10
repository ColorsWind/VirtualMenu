package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.Getter;
import lombok.ToString;

/**
 * 代表 {@link IUser} 点击菜单的事件.
 * @author colors_wind
 * @date 2020-02-10
 */
@Getter
@ToString(callSuper = true)
public class PacketMenuClickEvent extends PacketMenuEvent {

	protected final IconActionEvent event;
	
	public PacketMenuClickEvent(IUser<?> user, IPacketMenu menu, IconActionEvent event) {
		super(user, menu);
		this.event = event;
	}

	



}
