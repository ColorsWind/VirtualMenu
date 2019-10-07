package com.blzeecraft.virtualmenu.core.icon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

import lombok.ToString;
import lombok.val;

/**
 * 动态 {@link Icon}, 这个类是不可变的
 * @author colors_wind
 *
 */
@ToString(callSuper=true)
public class DynamicIcon extends SimpleIcon {
	public static final Function<IUser<?>, String> EMPTY_NAME = u -> null;
	public static final Function<IUser<?>, List<String>> EMPTY_LORE = u -> Collections.emptyList();

	protected final Function<IUser<?>, String> name;
	protected final Function<IUser<?>, List<String>> lore;

	public DynamicIcon(int priority, AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command, Function<IUser<?>, String> name,
			Function<IUser<?>, List<String>> lore) {
		super(priority, cache, clickCondition, viewCondition, command);
		this.name = name;
		this.lore = lore;
	}

	public DynamicIcon(AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command, Function<IUser<?>, String> name,
			Function<IUser<?>,List<String>> lore) {
		this(0, cache, clickCondition, viewCondition, command, name, lore);
	}

	public DynamicIcon(int priority, AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command,
			BiFunction<String, IUser<?>, String> replacer) {
		this(priority, cache, clickCondition, viewCondition, command, user -> replacer.apply(cache.getName(), user),
				user -> {
					val lores = cache.getCopyOfLore();
					for(int i=0;i<lores.length;i++) {
						lores[i] = replacer.apply(lores[i], user);
					}
					return Arrays.asList(lores);
				});
	}
	
	public DynamicIcon(AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command,
			BiFunction<String, IUser<?>, String> replacer) {
		this(0, cache, clickCondition, viewCondition, command, replacer);
	}

	@Override
	public AbstractItem<?> view(IUser<?> user) {
		if (canView(user)) {
			return user.getPlayerCache().viewItem.computeIfAbsent(this, k -> update0(user));
		}
		return VirtualMenu.emptyItem();
	}

	@Override
	public AbstractItem<?> update(IUser<?> user) {
		if (canView(user)) {
			return update0(user);
		}
		return VirtualMenu.emptyItem();

	}

	protected AbstractItem<?>update0(IUser<?> user) {
		val builder = this.cache.builder();
		builder.name(name.apply(user));
		builder.lore(lore.apply(user));
		val item = builder.build();
		user.getPlayerCache().viewItem.replace(this, item);
		return item;
	}
	
	
	
	

}
