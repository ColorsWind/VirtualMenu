package com.blzeecraft.virtualmenu.core.menu;

import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.val;

public class Condition {
	
	private final Predicate<IUser<?>>[] conditions;
	
	public boolean test(IUser<?> user) {
		for(val predicate : conditions) {
			if (!predicate.test(user)) {
				return false;
			}
		}
		return true;
	}

}
