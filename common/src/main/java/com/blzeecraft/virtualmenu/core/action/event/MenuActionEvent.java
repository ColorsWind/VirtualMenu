package com.blzeecraft.virtualmenu.core.action.event;

import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.user.UserSession;

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
