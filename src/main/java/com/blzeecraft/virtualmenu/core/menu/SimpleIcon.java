package com.blzeecraft.virtualmenu.core.menu;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.IUser;

public class SimpleIcon implements Icon {
	
	protected final AbstractItem<?> cache;
	protected final Condition condition;

	@Override
	public Optional<AbstractItem<?>> view(IUser<?> user) {
		if (condition.test(user)) {
			return Optional.of(cache);
		}
		return Optional.empty();
	}

	@Override
	public boolean canView(IUser<?> user) {
		return condition.test(user);
	}

	@Override
	public Optional<String> canClick(IUser<?> user) {
		// TODO Auto-generated method stub
		return null;
	}


}
