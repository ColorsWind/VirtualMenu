package com.blzeecraft.virtualmenu.core;

import com.blzeecraft.virtualmenu.core.menu.EventType;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper=true)
public class MenuActionEvent extends MenuEvent {
	protected final EventType eventType;

	public MenuActionEvent(UserSession session, EventType eventType) {
		super(session);
		this.eventType = eventType;
	}

}
