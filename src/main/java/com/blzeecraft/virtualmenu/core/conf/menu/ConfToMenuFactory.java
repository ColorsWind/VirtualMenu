package com.blzeecraft.virtualmenu.core.conf.menu;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf.IconConf;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.icon.SimpleIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
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
		LogNode iNode = node.sub("icons");
		for(Entry<String, IconConf> entry : conf.icons.entrySet()) {
			val iConf = entry.getValue();
			val sNode = iNode.sub(entry.getKey());
			val optSlot = calculateSlot(iConf);
			if (! optSlot.isPresent()) {
				PluginLogger.severe(sNode, "未配置Icon位置,已忽略这个Icon.");
				continue;
			}
			int slot = optSlot.getAsInt();
			if (slot < 0 || slot >= icons.length) {
				PluginLogger.severe(sNode, "Slot范围错误,允许的范围为: 0-" + (icons.length -1) + ",而设置是: " + slot + ",已忽略这个Icon.");
			}
			Icon icon = readIcon(sNode, iConf);
			if (icons[slot] == null) {
				icons[slot] = icon;
			} else {
				icons[slot] = MultiIcon.of(icons[slot], icon);
			}
		}
		PacketMenu menu = builder.build();
		return menu;

	}
	
	public static OptionalInt calculateSlot(IconConf conf) {
		if (conf.slot.isPresent()) {
			return conf.slot;
		} else if (conf.postion_x.isPresent() && conf.postion_y.isPresent()) {
			return OptionalInt.of(conf.postion_x.getAsInt() - 1 + (conf.postion_y.getAsInt() - 1) * 9);
		} 
		return OptionalInt.empty();
	}

	public static Icon readIcon(LogNode node, IconConf conf) {
		int priority = conf.priority.orElse(0); //default 0
		AbstractItem<?> cache = readItem(node, conf);
		ICondition clickCondition = Conditions.parse(node.sub("click-condition"), conf.click_condition);
		ICondition viewCondition = Conditions.parse(node.sub("view-condition"), conf.view_condition);
		IAction action = Actions.parse(node.sub("action"), conf.action);
		return new SimpleIcon(priority, cache, clickCondition, viewCondition, action);
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

	


}
