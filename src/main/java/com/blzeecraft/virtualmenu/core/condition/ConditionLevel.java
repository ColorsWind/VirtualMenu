package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

import lombok.ToString;
import lombok.val;

/**
 * 关于玩家等级的条件
 * @author colors_wind
 *
 */
@ToString
public class ConditionLevel extends Condition {
	protected final int level;
	protected final boolean take;

	public ConditionLevel(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.level = rlc.getAsInt("level");
		this.take = rlc.getAsOptBoolean("take").orElse(Boolean.FALSE).booleanValue();
		
	}

	@Override
	public Optional<String> check(ClickEvent e) {
		val user = e.getUser();
		int newLevel = user.getLevel() - level;
		if (newLevel < 0) {
			return Optional.of(message);
		}
		if (take) {
			user.setLevel(newLevel);
		}
		return Optional.empty();
	}
}
