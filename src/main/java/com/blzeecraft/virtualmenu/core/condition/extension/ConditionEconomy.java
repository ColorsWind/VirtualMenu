package com.blzeecraft.virtualmenu.core.condition.extension;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.MenuEvent;
import com.blzeecraft.virtualmenu.core.condition.Condition;
import com.blzeecraft.virtualmenu.core.conf.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

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
	public Optional<String> check(MenuEvent e) {
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
