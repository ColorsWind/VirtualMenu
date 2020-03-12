package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionConsoleCommand extends Action {
	public static final String KEY = "console";
	protected final String command;

	public ActionConsoleCommand(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.command = rlc.getAsString("command");
	}

	@Override
	public void execute(IUser<?> user) {
		VirtualMenu.getConsole().performCommand(command.replace("<player>", user.getName()));
	}

	@Override
	public String getKey() {
		return KEY;
	}

	public static String remap(String value) {
		return KEY + new ResolvedLineConfig("command", value).toString();
	}
	
	


}
