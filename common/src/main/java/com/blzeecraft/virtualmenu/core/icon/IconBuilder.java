package com.blzeecraft.virtualmenu.core.icon;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.val;

public class IconBuilder {
	
	private int priority;
	private AbstractItem<?> item;
	private Function<MenuEvent, Optional<String>> clickCondition;
	private Predicate<MenuEvent> viewCondition;
	private Consumer<MenuEvent> command;
	
	public IconBuilder priority(int priority) {
		this.priority = priority;
		return this;
	}
	public IconBuilder item(AbstractItem<?> item) {
		this.item = item;
		return this;
	}
	public IconBuilder clickCondition(Function<MenuEvent, Optional<String>> clickCondition) {
		this.clickCondition = clickCondition;
		return this;
	}
	public IconBuilder viewCondition(Predicate<MenuEvent> viewCondition) {
		this.viewCondition = viewCondition;
		return this;
	}
	public IconBuilder command(Consumer<MenuEvent> command) {
		this.command = command;
		return this;
	}
	
	public Icon build(LogNode node) {
		val variables = VirtualMenu.getVariableAdapter();
		if (variables.containsVariable(item.getName()) || variables.containsVariable(item.getLore())) {
			Function<IUser<?>, String> name;
			// displayname
			if (variables.containsVariable(item.getName())) {
				name = user -> variables.replace(user, item.getName());
			} else {
				name = user -> item.getName();
			}
			// lore
			Function<IUser<?>, List<String>> lore;
			if (variables.containsVariable(item.getLore())) {
				lore = user -> variables.replace(user, item.getLore());
			} else {
				lore = user -> item.getLore();
			}
			return new DynamicIcon(node, priority, item, clickCondition, viewCondition, command, name, lore);
		} else {
			return new SimpleIcon(node, priority, item, clickCondition, viewCondition, command);
		}
	}
	
	

}
