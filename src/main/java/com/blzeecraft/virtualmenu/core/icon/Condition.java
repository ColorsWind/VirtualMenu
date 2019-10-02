package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.val;

/**
 * 代表关于玩家的条件, 这个类是不可变的
 * @author colors_wind
 *
 */
public class Condition implements Function<IUser<?>, Optional<String>>, Predicate<IUser<?>> {

	public static final Condition ALLOW_ALL = new Condition(u -> Optional.empty());
	public static final Condition ALLOW_DENY = new Condition(u -> Optional.of(""));
	
	private final Function<IUser<?>, Optional<String>>[] conditions;
	
	@SafeVarargs
	public Condition(Function<IUser<?>, Optional<String>>... conditions) {
		this.conditions = conditions;
	}
	
	@Override
	public Optional<String> apply(IUser<?> user) {
		for(val cd : conditions) {
			val opt  = cd.apply(user);
			if (opt.isPresent()) {
				return opt;
			}
		}
		return Optional.empty();
	}

	@Override
	public boolean test(IUser<?> user) {
		return !apply(user).isPresent();
	}



}
