package com.blzeecraft.virtualmenu.core.conf.menu;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalInt;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.ActionUtils;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.condition.ConditionUtils;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf.IconConf;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.IconBuilder;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;
import com.blzeecraft.virtualmenu.core.menu.PacketMenuBuilder;
import com.blzeecraft.virtualmenu.core.variable.UpdateDelay;

import lombok.val;
import net.md_5.bungee.api.ChatColor;

public class ConfMenuFactory {
	public static Map<ClickType, ICondition> EMPTY_CONDITION = Arrays.stream(ClickType.values()).map(type -> {
		val m = new EnumMap<ClickType, ICondition>(ClickType.class);
		m.put(type, ConditionUtils.EMPTY_CONDITION);
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
		builder.title(title);
		// type
		IMenuType type = VirtualMenu.getMenuType(conf.global.type).orElseGet(() -> {
			PluginLogger.warning(gNode, "找不到菜单类型: " + conf.global.type);
			return VirtualMenu.getMenuTypes()[0];
		});
		builder.type(type);
		// refresh
		String refresh = conf.global.refresh.orElse(UpdateDelay.NEVER.name());
		builder.refresh(UpdateDelay.get(refresh).orElseGet(() -> {
			PluginLogger.warning(node, "找不到刷新间隔类型: " + refresh + " 可用的类型: " + UpdateDelay.typesToString());
			return UpdateDelay.NORMAL;
		}));
		// bound not yet

		/* ########## events ########## */
		LogNode eNode = node.sub("events");
		conf.events.forEach((k, v) -> {
			try {
				EventType eventType = EventType.valueOf(k);
				IAction actions = ActionUtils.parse(eNode.sub("actions"), v.action);
				ICondition conditions = ConditionUtils.parse(eNode.sub("conditions"), v.condition);
				IAction handler = ActionUtils.wrap(actions, conditions);
				builder.addEventHandler(eventType, handler);
			} catch (IllegalArgumentException e) {
				PluginLogger.warning(eNode, "未知事件类型: " + k + ", 已经忽略.");
			}
		});

		/* ########## icons ########## */
		Icon[] icons = new Icon[type.getSize()];
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
		builder.icon(icons);
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
		val builder = new IconBuilder();
		int priority = conf.priority.orElse(0); //default 0
		builder.priority(priority);
		AbstractItem<?> cache = readItem(node, conf);
		builder.item(cache);
		ICondition clickCondition = ConditionUtils.parse(node.sub("click-condition"), conf.click_condition);
		builder.clickCondition(clickCondition);
		ICondition viewCondition = ConditionUtils.parse(node.sub("view-condition"), conf.view_condition);
		builder.viewCondition(viewCondition);
		IAction action = ActionUtils.parse(node.sub("action"), conf.action);
		builder.command(action);
		return builder.build(node);
	}
	
	public static AbstractItem<?> readItem(LogNode node, IconConf conf) {
		val builder = VirtualMenu.createItemBuilder();
		builder.id(conf.id);
		builder.amount(conf.amount.orElse(1));
		builder.nbt(conf.nbt.orElse(null));
		builder.name(conf.name.orElse(null));
		builder.lore(conf.lore);
		AbstractItem<?> item = builder.build(node);
		return item;
	}

	


}
