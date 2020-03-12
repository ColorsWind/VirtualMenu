package com.blzeecraft.virtualmenu.core.icon;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.ConfSerializable;
import com.blzeecraft.virtualmenu.core.conf.StringSerializable;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf.IconConf;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
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
public class SimpleIcon implements Icon, ConfSerializable<IconConf> {
	
	protected final LogNode node;
	protected final int priority;
	protected final AbstractItem<?> cache;
	protected final Function<MenuEvent, Optional<String>> clickCondition;
	protected final Predicate<MenuEvent> viewCondition;
	protected final Consumer<MenuEvent> command;
	
	public SimpleIcon(LogNode node, AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command) {
		this(node, 0, cache, clickCondition, viewCondition, command);
	}
	
	
	public SimpleIcon(LogNode node, int priority, AbstractItem<?> cache, ICondition clickCondition,
			ICondition viewCondition, IAction command) {
		this.node = node;
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
	public IconConf serialize() {
		IconConf conf = new IconConf();
		conf.priority = OptionalInt.of(priority);
		cache.applyConf(conf);;
		if (clickCondition instanceof StringSerializable) {
			conf.click_condition = Arrays.asList(((StringSerializable) clickCondition).seriablizeAll());
		}
		if (viewCondition instanceof StringSerializable) {
			conf.view_condition = Arrays.asList(((StringSerializable) viewCondition).seriablizeAll());
		}
		if (command instanceof StringSerializable) {
			conf.action = Arrays.asList(((StringSerializable) command).seriablizeAll());
		}
		return conf;
	}


	@Override
	public IconConf[] seriablizeAll() {
		return new IconConf[] {serialize()};
	}






}
