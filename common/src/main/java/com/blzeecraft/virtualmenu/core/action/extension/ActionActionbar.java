package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionActionbar extends Action {
	protected final String message;

	public ActionActionbar(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.message = rlc.getAsString("s");
	}

	@Override
	public void execute(IUser<?> user) {
		user.sendActionbar(message);

	}

	@Override
	public String getKey() {
		return "actionbar";
	}

}
