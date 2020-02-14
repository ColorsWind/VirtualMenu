package com.blzeecraft.virtualmenu.core.menu;

import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.condition.ICondition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventHandler implements Consumer<IconActionEvent> {
	protected final ICondition condition;
	protected final IAction action;
	
	
	@Override
	public void accept(IconActionEvent e) {
		if (condition.test(e)) {
			action.accept(e);
		}
		
	}

}
