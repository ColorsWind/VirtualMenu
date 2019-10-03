package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

public class ActionActionbar extends AbstractAction {
	protected final String message;

	public ActionActionbar(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.message = rlc.getAsString("s");
	}

	@Override
	public void execute(IUser<?> user) {
		user.sendActionbar(message);

	}

}
