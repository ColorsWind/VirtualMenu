package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.google.gson.JsonObject;

public abstract class AbstractAction implements LoggerObject {

	protected final LogNode node;

	
	public AbstractAction(LogNode node, JsonObject json) {
		this(node);
	}
	
	public AbstractAction(LogNode node) {
		this.node = node;
	}
	
	public abstract ActionType getType();
	
	@Override
	public LogNode getLogNode() {
		return node;
	}
	

	public abstract void execute(IUser<?> user);

}
