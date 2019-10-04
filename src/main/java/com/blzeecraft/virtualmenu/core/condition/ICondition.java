package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

public interface ICondition extends Function<ClickEvent, Optional<String>>, Predicate<ClickEvent> {

	@Override
	public default boolean test(ClickEvent e) {
		return !this.apply(e).isPresent();
	}
}
