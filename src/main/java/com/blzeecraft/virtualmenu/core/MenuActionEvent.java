package com.blzeecraft.virtualmenu.core;

import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper=true)
public class MenuActionEvent extends MenuEvent {
	protected final EventType eventType;

	public MenuActionEvent(IUser<?> user, IPacketMenu menu, EventType eventType) {
		super(user, menu);
		this.eventType = eventType;
	}

}
