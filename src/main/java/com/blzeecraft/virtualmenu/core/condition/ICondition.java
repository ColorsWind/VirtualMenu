package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IScope;
import com.blzeecraft.virtualmenu.core.MenuEvent;

public interface ICondition extends Function<MenuEvent, Optional<String>>, Predicate<MenuEvent>, IScope {

	@Override
	default boolean test(MenuEvent event) {
		return !this.apply(event).isPresent();
	}
	
}
