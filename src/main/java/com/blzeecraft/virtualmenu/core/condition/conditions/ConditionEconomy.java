package com.blzeecraft.virtualmenu.core.condition.conditions;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.condition.Condition;
import com.blzeecraft.virtualmenu.core.config.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;

import lombok.ToString;
import lombok.val;

/**
 * 关于玩家经济的条件
 * @author colors_wind
 *
 */
@ToString
public class ConditionEconomy extends Condition {
	protected final double amount;
	protected final String currency;
	protected final boolean take;

	public ConditionEconomy(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.amount = rlc.getAsInt("amount");
		this.currency = rlc.getAsOptString("currency").orElse("");
		this.take = rlc.getAsOptBoolean("take").orElse(Boolean.FALSE).booleanValue();
	}

	@Override
	public Optional<String> check(IconActionEvent e) {
		val user = e.getUser();
		if(take) {
			return user.withdraw(currency, amount) ? Optional.empty() : Optional.of(message);
		}
		val opt = user.getBanlance(currency);
		if (opt.isPresent() && opt.getAsDouble() > amount) {
			return Optional.empty();
		}
		return Optional.of(message);
	}

}
