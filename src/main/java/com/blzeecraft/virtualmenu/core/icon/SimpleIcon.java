package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.val;

/**
 * 简单(静态)的 {@link Icon} 
 * @author colors_wind
 *
 */
@AllArgsConstructor
@ToString
public class SimpleIcon implements Icon {
	
	protected final int priority;
	protected final AbstractItem<?> cache;
	protected final Function<IUser<?>, Optional<String>> clickCondition;
	protected final Predicate<IUser<?>> viewCondition;
	protected final BiConsumer<IUser<?>, ClickType> command;
	
	public SimpleIcon(AbstractItem<?> cache, Function<IUser<?>, Optional<String>> clickCondition,
			Predicate<IUser<?>> viewCondition, BiConsumer<IUser<?>, ClickType> command) {
		this(0, cache, clickCondition, viewCondition, command);
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
		return viewCondition.test(user);
	}

	@Override
	public Optional<String> canClick(IUser<?> user) {
		return clickCondition.apply(user);
	}

	@Override
	public void click(ClickEvent e) {
		val user = e.getUser();
		if (canView(user)) {
			val deny = clickCondition.apply(user);
			if (deny.isPresent()) {
				user.sendMessage(deny.get());
			} else {
				command.accept(user, e.getType());
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
