package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.user.UserSession;

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
	public AbstractItem<?> view(UserSession session) {
		if (canView(session)) {
			return cache;
		}
		return VirtualMenu.emptyItem();
	}

	@Override
	public boolean canView(UserSession session) {
		return viewCondition.test(new MenuEvent(session));
	}

	@Override
	public Optional<String> canClick(IconActionEvent e) {
		return clickCondition.apply(e);
	}

	@Override
	public void accept(IconActionEvent e) {
		val session = e.getSession();
		val user = e.getUser();
		if (canView(session)) {
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
	public AbstractItem<?> refreshItem(UserSession session) {
		return view(session);
	}

}
