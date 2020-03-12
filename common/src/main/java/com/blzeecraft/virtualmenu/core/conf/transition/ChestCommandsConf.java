package com.blzeecraft.virtualmenu.core.conf.transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.animation.EnumUpdateDelay;
import com.blzeecraft.virtualmenu.core.conf.ObjectWrapper;
import com.blzeecraft.virtualmenu.core.conf.menu.ChestCommandsAdapter;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.EventType;

import lombok.val;

public class ChestCommandsConf {
	private final Map<String, Object> map;;
	private final StandardConf conf;
	private final LogNode node;

	public ChestCommandsConf(LogNode node, Map<String, Object> map) {
		this.map = map;
		this.node = node;
		this.conf = new StandardConf();
	}

	public StandardConf remap() {
		Map<String, Object> global = new ObjectWrapper(map.get("menu-settings")).asS2ObjectMap();
		// menu-settings
		conf.global.title = new ObjectWrapper(global.get("name")).asString();
		conf.global.type = Arrays.stream(VirtualMenu.getMenuTypes())
				.filter(menu -> menu.getSize() == new ObjectWrapper(global.get("rows")).asInteger()).findFirst()
				.orElseGet(() -> {
					PluginLogger.warning(node, "无法匹配MenuType, 请检查rows是否为=9,18,27,36,45,54中的一个.");
					return VirtualMenu.getMenuTypes()[0];
				}).getType();
		conf.global.refresh = Optional.of(EnumUpdateDelay
				.saftyGet(new ObjectWrapper(global.get("auto-refresh")).asOptInteger().orElse(20)).name());
		val eConf = new StandardConf.EventConf();
		eConf.action = new ObjectWrapper(global.get("open-action")).asStringList().stream().map(ChestCommandsAdapter::remap)
				.collect(Collectors.toCollection(ArrayList::new));
		conf.events.put(EventType.OPEN_MENU.name(), eConf);
		// icons
		val iconMap = new LinkedHashMap<String, StandardConf.IconConf>();
		map.entrySet().stream().filter(entry -> !"menu-settings".equals(entry.getKey())).forEach(entry -> {
			val key = entry.getKey();
			val value = new ObjectWrapper(entry.getValue());
			val iconConf = new StandardConf.IconConf();
			val iconNode = node.sub(key);
			try {
				iconConf.action = value.getValue("COMMAND").asStringList().stream().map(ChestCommandsAdapter::remap)
						.collect(Collectors.toList());
				iconConf.name = value.getValue("NAME").asOptString();
				iconConf.lore = value.getValue("LORE").asStringList();
				iconConf.id = value.getValue("ID").asString();
				iconConf.postion_x = value.getValue("POSITION-X").asOptInteger();
				iconConf.postion_y = value.getValue("POSITION-Y").asOptInteger();
				iconMap.put(key, iconConf);
			} catch (Exception e) {
				PluginLogger.severe(iconNode, "处理ChestCommands格式菜单出现异常.");
				e.printStackTrace();
			}
		});
		conf.icons = iconMap;
		return conf;
	}

}
