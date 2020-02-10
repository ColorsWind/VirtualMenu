package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuEvent;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.val;

/**
 * 简单(静态)的 {@link Icon} 
 * @author colors_wind
 *
 */
@ToString
@AllArgsConstructor
public class SimpleIcon implements Icon {
	
	protected final int priority;
	protected final AbstractItem<?> cache;
	protected final Function<MenuEvent, Optional<String>> clickCondition;
	protected final Predicate<MenuEvent> viewCondition;
	protected final Consumer<MenuEvent> command;
	
	public SimpleIcon(AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command) {
		this(0, cache, clickCondition, viewCondition, command);
	}
	
	
	public SimpleIcon(int priority, AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command) {
		super();
		this.priority = priority;
		this.cache = cache;
		this.clickCondition = clickCondition;
		this.viewCondition = viewCondition;
		this.command = command;
	}


	@Override
	public AbstractItem<?> view(IUser<?> user) {
		if (canView(user)) {
			return cache;
		}
		return VirtualMenu.emptyItem();
	}

	@Override
	public boolean canView(IUser<?> user) {
		return viewCondition.test(user.getViewEvent());
	}

	@Override
	public Optional<String> canClick(IconActionEvent e) {
		return clickCondition.apply(e);
	}

	@Override
	public void accept(IconActionEvent e) {
		val user = e.getUser();
		if (canView(user)) {
			val deny = clickCondition.apply(e);
			if (deny.isPresent()) {
				user.sendMessage(deny.get());
			} else {
				command.accept(e);
			}
		}
		
	}

	@Override
	public int getPriority() {
		return this.priority;
	}


	@Override
	public AbstractItem<?> update(IUser<?> user) {
		return view(user);
	}

}
