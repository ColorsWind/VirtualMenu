package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.ToString;

@ToString
public class ActionTell extends Action {

	protected final String message;

	public ActionTell(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.message = rlc.getAsString("s");
	}

	@Override
	public void execute(IUser<?> user) {
		user.sendMessage(message.replace("<player>", user.getName()));
	}


}
