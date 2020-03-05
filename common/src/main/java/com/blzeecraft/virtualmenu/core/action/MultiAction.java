package com.blzeecraft.virtualmenu.core.action;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.core.Command;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.conf.StringSerializable;

import lombok.val;

/**
 * 用于包装多个动作, 这个类是不可变的.
 * 
 * @author colors_wind
 * @date 2020-02-07
 */
public class MultiAction implements IAction, StringSerializable  {
	
	protected final List<Action> actions;

	public MultiAction(List<Action> actions) {
		this.actions = new ArrayList<>(actions);
	}

	@Override
	public void accept(MenuEvent e) {
		for(val action : actions) {
			action.accept(e);
		}
	}
	
	@Override
	public String[] seriablizeAll() {
		return actions.stream().map(Command::serialize).toArray(String[]::new);
	}

	@Override
	public String serialize() {
		throw new UnsupportedOperationException();
	}
}
