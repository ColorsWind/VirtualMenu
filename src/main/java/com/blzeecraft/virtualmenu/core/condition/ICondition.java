package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

public interface ICondition extends Function<IconActionEvent, Optional<String>>, Predicate<IconActionEvent> {

	@Override
	public default boolean test(IconActionEvent e) {
		return !this.apply(e).isPresent();
	}
	
	public default ClickType[] scope() {
		return ClickType.values();
	}
}
