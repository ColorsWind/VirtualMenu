package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionTell extends Action {
	public static final String KEY = "tell";
	protected final String message;

	public ActionTell(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.message = rlc.getAsString("s");
	}

	@Override
	public void execute(IUser<?> user) {
		user.sendMessage(message.replace("<player>", user.getName()));
	}

	@Override
	public String getKey() {
		return KEY;
	}

	public static String remap(String value) {
		return KEY + new ResolvedLineConfig("tell", value).toString();
	}

	

}
