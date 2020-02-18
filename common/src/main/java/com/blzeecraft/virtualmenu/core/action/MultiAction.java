package com.blzeecraft.virtualmenu.core.action;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;

import lombok.val;

/**
 * 用于包装多个动作, 这个类是不可变的.
 * 
 * @author colors_wind
 * @date 2020-02-07
 */
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
