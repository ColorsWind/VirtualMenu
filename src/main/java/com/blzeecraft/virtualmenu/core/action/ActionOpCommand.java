package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.ToString;

@ToString
public class ActionOpCommand extends Action {

	protected final String command;

	public ActionOpCommand(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.command = rlc.getAsString("command");
	}

	@Override
	public void execute(IUser<?> user) {
		user.performCommand(command.replace("<player>", user.getName()));
	}


}
