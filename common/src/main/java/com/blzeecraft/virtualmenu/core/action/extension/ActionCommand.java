package com.blzeecraft.virtualmenu.core.action.extension;


import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionCommand extends Action {
	public static final String KEY = "command";
	protected final String command;

	public ActionCommand(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.command = rlc.getAsString("command");
	}

	@Override
	public void execute(IUser<?> user) {
		user.performCommand(command.replace("<player>", user.getName()));
	}

	@Override
	public String getKey() {
		return KEY;
	}
	
	public static String remap(String s) {
		return KEY + new ResolvedLineConfig("command", s).toString();
	}



}
