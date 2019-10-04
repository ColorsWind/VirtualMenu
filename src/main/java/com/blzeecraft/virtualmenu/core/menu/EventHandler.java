package com.blzeecraft.virtualmenu.core.menu;

import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.action.AbstractAction;
import com.blzeecraft.virtualmenu.core.condition.Condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventHandler implements Consumer<ClickEvent> {
	protected final Condition condition;
	protected final AbstractAction action;
	
	
	@Override
	public void accept(ClickEvent e) {
		if (condition.test(e)) {
			action.accept(e);
		}
		
	}

}
