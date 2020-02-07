package com.blzeecraft.virtualmenu.core.conf.menu;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf.IconConf;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;
import com.blzeecraft.virtualmenu.core.menu.PacketMenuBuilder;

import lombok.val;
import net.md_5.bungee.api.ChatColor;

public class ConfToMenuFactory {
	public static Map<ClickType, ICondition> EMPTY_CONDITION = Arrays.stream(ClickType.values()).map(type -> {
		val m = new EnumMap<ClickType, ICondition>(ClickType.class);
		m.put(type, Conditions.EMPTY_CONDITION);
		return m;
	}).reduce((map1, map2) -> {
		map1.putAll(map2);
		return map1;
	}).get();

	public static PacketMenu convert(LogNode node, StandardConf conf) {
		PacketMenuBuilder builder = new PacketMenuBuilder(node);
		/* ########## global ########## */
		LogNode gNode = node.sub("global");
		// title
		String title = conf.global.title;
		title = ChatColor.translateAlternateColorCodes('&', title);
		// type
		IMenuType type = VirtualMenu.getMenuType(conf.global.type).orElseGet(() -> {
			PluginLogger.warning(gNode, "找不到菜单类型: " + conf.global.type);
			return VirtualMenu.getMenuTypes()[0];
		});
		builder.type(type);
		// refresh
		int refresh = conf.global.refresh;
		builder.refresh(refresh);
		// bound not yet

		/* ########## events ########## */
		LogNode eNode = node.sub("events");
		conf.events.forEach((k, v) -> {
			try {
				EventType eventType = EventType.valueOf(k);
				IAction actions = Actions.parse(eNode.sub("actions"), v.action);
				ICondition conditions = Conditions.parse(eNode.sub("conditions"), v.condtion);
				IAction handler = Actions.wrap(actions, conditions);
				builder.addEventHandler(eventType, handler);
			} catch (IllegalArgumentException e) {
				PluginLogger.warning(eNode, "未知事件类型: " + k + ", 已经忽略.");
			}
		});

		/* ########## icons ########## */
		Icon[] icons = new Icon[type.size()];
		conf.icons.forEach((k, v) -> {

		});

		PacketMenu menu = builder.build();
		return menu;

	}

	public static Icon readIcon(LogNode node, IconConf conf) {
		int priority = conf.priority;
		AbstractItem<?> cache = readItem(node, conf);
		//Function<ClickEvent, Optional<String>> clickCondition = readCondition(node.sub("view-condition"), conf.view_condition);

	}
	
	public static AbstractItem<?> readItem(LogNode node, IconConf conf) {
		AbstractItemBuilder<?> iBuilder = VirtualMenu.createItemBuilder();
		iBuilder.id(conf.id);
		iBuilder.amount(conf.amount);
		iBuilder.nbt(conf.nbt);
		iBuilder.name(conf.name);
		iBuilder.lore(conf.lore);
		AbstractItem<?> item = iBuilder.build();
		return item;
	}

	public static Map<ClickType, ICondition> readCondition(LogNode node, Map<String, List<String>> map) {
		EnumMap<ClickType, ICondition> enumMap = new EnumMap<>(EMPTY_CONDITION);
		map.forEach((k, v) -> {
			try {
				ClickType type = ClickType.valueOf(k);
				ICondition condition = Conditions.parse(node.sub(k), v);
				enumMap.put(type, condition);
			} catch (IllegalArgumentException e) {
				PluginLogger.warning(node, "未知ClickType: " + k + ", 忽略.");
			}
		});

		return enumMap;
	}

	public static Predicate<ClickEvent> wrapCondition(Map<ClickType, ICondition> map) {
		return event -> map.get(event.getType()).test(event);
	}

}
