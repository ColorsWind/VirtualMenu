package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;

/**
 * 代表关于玩家的条件, 这个类是不可变的
 * @author colors_wind
 *
 */
public abstract class Condition implements Function<IUser<?>, Optional<String>>, Predicate<IUser<?>>, LoggerObject {

	protected final LogNode node;
	protected final String message;

	public Condition(LogNode node, ResolvedLineConfig rlc) {
		this.node = node;
		this.message = rlc.getAsOptString("msg").orElse("");
	}

	public Condition(LogNode node) {
		this.node = node;
		this.message = "";
	}

	@Override
	public LogNode getLogNode() {
		return node;
	}

	@Override
	public boolean test(IUser<?> user) {
		return !this.apply(user).isPresent();
	}

	
	
}
