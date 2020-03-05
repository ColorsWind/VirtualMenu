package com.blzeecraft.virtualmenu.core;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.StringSerializable;
import com.blzeecraft.virtualmenu.core.conf.line.LineConfigObject;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

public abstract class Command extends LineConfigObject implements ICondition, IAction, StringSerializable {

	public Command(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
	}
	
	public Command(LogNode node) {
		this(node, new ResolvedLineConfig());
	}

	@Override
	public void accept(MenuEvent event) {
		apply(event);
	}

	@Override
	public abstract Optional<String> apply(MenuEvent event);

	public abstract String getKey();
	
	
	@Override
	public String serialize() {
		return new StringBuilder(this.getKey()).append(this.rlc.toString()).toString();
	}

	@Override
	public String[] seriablizeAll() {
		return new String[] {serialize()};
	}
	
}
