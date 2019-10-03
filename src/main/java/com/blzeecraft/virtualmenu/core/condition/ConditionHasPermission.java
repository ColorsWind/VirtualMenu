package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;

public class ConditionHasPermission extends Condition implements LoggerObject {

	protected final String permission;
	protected final String message;

	public ConditionHasPermission(LogNode node, ResolvedLineConfig rlc) {
		super(node);
		this.permission = rlc.getAsString("perm");
		this.message = rlc.getAsOptString("msg").orElse("");
	}

	@Override
	public Optional<String> apply(IUser<?> user) {
		return user.hasPermission(permission) ? Optional.empty() : Optional.of(message);
	}

}
