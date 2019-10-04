package com.blzeecraft.virtualmenu.core.menu;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.yaml.snakeyaml.Yaml;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.Condition;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;

public class YamlParser implements BiFunction<LogNode, Reader, AbstractPacketMenu> {

	@SuppressWarnings("unchecked")
	@Override
	public AbstractPacketMenu apply(LogNode node, Reader reader) {
		Yaml yaml = new Yaml();
		Map<String, Object> config = yaml.load(reader);
		// 下面读取global
		Map<String, Object> global = (Map<String, Object>) config.get("global");
		String typeS = (String) global.get("type");
		IMenuType type = VirtualMenu.getMenuType(typeS); // 类型
		val builder = new PacketMenuBuilder(node, type);
		String title = (String) global.get("title");
		builder.title(title);
		int refresh = (Integer)global.getOrDefault("refresh", -1); // 刷新时间
		builder.refresh(refresh);
		// 下面读取events
		Map<String, Object> events = (Map<String, Object>) config.get("events");
		events.forEach((k, v) -> {
			try {
				val eventType = EventType.valueOf(k.toUpperCase());
				Map<String, Object> event = (Map<String, Object>) v;
				val conditions = readCondtion(node, event, "condition");
				val actions = readAction(node, event, "action");
				val handler = new EventHandler(conditions, actions);
				builder.addEventHandler(eventType, handler);
			} catch (IllegalArgumentException e) {}
		});
		// 下面读取icons
		return null;
	}

	@SuppressWarnings("unchecked")
	public ICondition readCondtion(LogNode node, Map<String, Object> map, String key) {
		Object o = map.get(key);
		if (o instanceof List) {
			return Conditions.parse(node.sub(key), (List<String>)o);
		}
		return Conditions.parse(node.sub(key), o.toString());
	}
	
	@SuppressWarnings("unchecked")
	public IAction readAction(LogNode node, Map<String, Object> map, String key) {
		Object o = map.get(key);
		if (o instanceof List) {
			return Actions.parse(node.sub(key), (List<String>)o);
		}
		return Actions.parse(node.sub(key), o.toString());
	}
}
