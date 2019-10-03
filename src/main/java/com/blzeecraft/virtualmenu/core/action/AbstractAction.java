package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;

public abstract class AbstractAction implements LoggerObject {

	protected final LogNode node;

	
	public AbstractAction(LogNode node, ResolvedLineConfig rlc) {
		this(node);
	}
	
	public AbstractAction(LogNode node) {
		this.node = node;
	}
	
	@Override
	public LogNode getLogNode() {
		return node;
	}
	

	public abstract void execute(IUser<?> user);

}
