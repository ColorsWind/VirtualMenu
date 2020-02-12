package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
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
