package com.blzeecraft.virtualmenu.core.action;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.core.MenuEvent;

import lombok.val;

public class MultiAction implements IAction {
	
	protected final List<IAction> actions;

	public MultiAction(List<IAction> actions) {
		this.actions = new ArrayList<>(actions);
	}

	@Override
	public void accept(MenuEvent e) {
		for(val action : actions) {
			action.accept(e);
		}
		
	}
}
