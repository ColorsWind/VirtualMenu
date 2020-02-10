package com.blzeecraft.virtualmenu.core.condition.conditions;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.condition.Condition;
import com.blzeecraft.virtualmenu.core.config.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;

import lombok.ToString;

/**
 * 关于玩家权限的条件
 * @author colors_wind
 *
 */
@ToString
public class ConditionPermission extends Condition {

	protected final String permission;

	public ConditionPermission(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.permission = rlc.getAsString("perm");

	}

	@Override
	public Optional<String> check(IconActionEvent e) {
		return e.getUser().hasPermission(permission) ? Optional.empty() : Optional.of(message);
	}

}
