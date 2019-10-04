package com.blzeecraft.virtualmenu.core.condition;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

/**
 * 代表关于玩家的条件, 这个类是不可变的
 * @author colors_wind
 *
 */
public abstract class Condition implements ICondition, LoggerObject {

	protected final LogNode node;
	protected final String message;
	protected final Set<ClickType> types;

	public Condition(LogNode node, ResolvedLineConfig rlc) {
		this.node = node;
		this.message = rlc.getAsOptString("msg").orElse("");
		this.types = rlc.getAsOptEnumSet("click", ClickType.class).orElse(EnumSet.allOf(ClickType.class));
	}

	public Condition(LogNode node) {
		this.node = node;
		this.message = "";
		this.types = EnumSet.allOf(ClickType.class);
	}

	@Override
	public LogNode getLogNode() {
		return node;
	}
	
	

	@Override
	public Optional<String> apply(ClickEvent e) {
		if (types.contains(e.getType())) {
			return Optional.of(message);
		}
		return check(e);
	}

	public abstract Optional<String> check(ClickEvent e);

	@Override
	public boolean test(ClickEvent e) {
		return !this.apply(e).isPresent();
	}

	
	
}
