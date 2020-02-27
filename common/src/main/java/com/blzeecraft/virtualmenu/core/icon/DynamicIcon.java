package com.blzeecraft.virtualmenu.core.icon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.ToString;
import lombok.val;

/**
 * 动态 {@link Icon}, 这个类是不可变的
 * 
 * @author colors_wind
 *
 */
@ToString(callSuper = true)
public class DynamicIcon extends SimpleIcon {
	public static final Function<IUser<?>, String> EMPTY_NAME = u -> null;
	public static final Function<IUser<?>, List<String>> EMPTY_LORE = u -> Collections.emptyList();

	protected final Function<IUser<?>, String> name;
	protected final Function<IUser<?>, List<String>> lore;

	public DynamicIcon(LogNode node, int priority, AbstractItem<?> cache, ICondition clickCondition, ICondition viewCondition,
			IAction command, Function<IUser<?>, String> name, Function<IUser<?>, List<String>> lore) {
		super(node, priority, cache, clickCondition, viewCondition, command);
		this.name = name;
		this.lore = lore;
	}

	public DynamicIcon(LogNode node, AbstractItem<?> cache, ICondition clickCondition, ICondition viewCondition, IAction command,
			Function<IUser<?>, String> name, Function<IUser<?>, List<String>> lore) {
		this(node, 0, cache, clickCondition, viewCondition, command, name, lore);
	}

	public DynamicIcon(LogNode node, int priority, AbstractItem<?> cache, ICondition clickCondition, ICondition viewCondition,
			IAction command, BiFunction<String, IUser<?>, String> replacer) {
		this(node, priority, cache, clickCondition, viewCondition, command, user -> replacer.apply(cache.getName(), user),
				user -> {
					String[]  lores = cache.getLore().toArray(new String[0]);
					for (int i = 0; i < lores.length; i++) {
						lores[i] = replacer.apply(lores[i], user);
					}
					return Arrays.asList(lores);
				});
	}

	public DynamicIcon(LogNode node, AbstractItem<?> cache, ICondition clickCondition, ICondition viewCondition, IAction command,
			BiFunction<String, IUser<?>, String> replacer) {
		this(node, 0, cache, clickCondition, viewCondition, command, replacer);
	}

	public AbstractItem<?> refreshItem(UserSession session) {
		if (canView(session)) {
			return refreshUncheck(session);
		}
		return VirtualMenu.emptyItem();

	}

	protected AbstractItem<?> refreshUncheck(UserSession session) {
		val builder = this.cache.builder();
		val user = session.getUser();
		builder.name(name.apply(user));
		builder.lore(lore.apply(user));
		val item = builder.build(this.getLogNode());
		return item;
	}

}
