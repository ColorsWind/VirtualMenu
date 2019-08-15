package com.blzeecraft.virtualmenu.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.blzeecraft.virtualmenu.action.AbstractAction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomActionLoadEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private final String tag;
	private final String raw;
	
	private AbstractAction action;
	
	public boolean register(AbstractAction action) {
		if (action == null) {
			this.action = action;
			return true;
		}
		return false;
	}
	
	public boolean isRegister() {
		return action != null;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}


	

}
