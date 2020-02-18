package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

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
	
	public PacketMenuClickEvent(UserSession session, IconActionEvent event) {
		super(session);
		this.event = event;
	}

	



}
