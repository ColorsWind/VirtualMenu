package com.blzeecraft.virtualmenu.core.conf.transition;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.conf.convert.ConvertFunctions;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.variable.UpdatePeriod;

import lombok.NonNull;
import lombok.val;

public class ChestCommandsConf {
	private final Map<String, Object> map;;
	private final StandardConf conf;
	private final LogNode node;

	public ChestCommandsConf(@NonNull LogNode node, @NonNull Map<String, Object> map) {
		this.map = map;
		this.node = node;
		this.conf = new StandardConf();
	}

	public StandardConf remap() {
		MenuSettings settings = (MenuSettings) ConvertFunctions.TO_SUBCONF.apply(MenuSettings.class,
				ConvertFunctions.TO_S2OBJECT_MAP.apply(map.get("menu-settings")));
		conf.global = settings.remapGlobal(node.sub("menu-settings"));
		conf.events = new LinkedHashMap<>();
		conf.events.put(EventType.OPEN_MENU.name(), settings.remapOpenAction(node.sub("open-actions")));
		conf.icons = new LinkedHashMap<>();
		ConvertFunctions.TO_S2OBJECT_MAP.apply(map.entrySet()).entrySet().stream()
				.filter(entry -> !"menu-settings".equals(entry.getKey())).forEach(entry -> {
					val key = entry.getKey();
					val sect = ConvertFunctions.TO_S2OBJECT_MAP.apply(entry.getValue());
					val icon = (Icon) ConvertFunctions.TO_SUBCONF.apply(Icon.class, sect);
					val iconConf = icon.remapIcon(node.sub(key));
					conf.icons.put(key, iconConf);
				});
		return conf;
	}

	public static class MenuSettings extends SubConf {
		public String name;
		public int rows;
		public Optional<Integer> auto_refresh;
		public List<String> open_action;

		public StandardConf.GlobalConf remapGlobal(LogNode node) {
			val conf = new StandardConf.GlobalConf();
			conf.title = this.name;
			conf.type = Arrays.stream(VirtualMenu.getMenuTypes()).filter(menu -> menu.getSize() == this.rows)
					.findFirst().orElseGet(() -> {
						PluginLogger.warning(node, "无法匹配MenuType, 请检查rows是否为=9,18,27,36,45,54中的一个.");
						return VirtualMenu.getMenuTypes()[0];
					}).getType();
			conf.refresh = Optional.of(auto_refresh.flatMap(UpdatePeriod::saftyGet).orElseGet(() -> {
				PluginLogger.warning(node, "无法匹配 UpdateDelay,将会使用: " + UpdatePeriod.NORMAL.toString());
				return UpdatePeriod.NORMAL;
			}).name());
			return conf;
		}

		@SuppressWarnings("unchecked")
		public StandardConf.EventConf remapOpenAction(LogNode node) {
			val conf = new StandardConf.EventConf();
			conf.condition = Collections.emptyList();
			conf.action = ((List<String>) ConvertFunctions.TO_LIST.apply(open_action, String.class)).stream()
					.map(ChestCommandsAdapter::remap).collect(Collectors.toList());
			return conf;
		}
	}

	public static class Icon extends SubConf {
		public String ID;
		public List<String> COMMAND;
		public Optional<String> NAME;
		public List<String> LORE;
		public int POSITION_X;
		public int POSITION_Y;

		public StandardConf.IconConf remapIcon(LogNode node) {
			val conf = new StandardConf.IconConf();
			conf.action = this.COMMAND.stream().map(ChestCommandsAdapter::remap).collect(Collectors.toList());
			conf.name = this.NAME;
			conf.lore = this.LORE;
			conf.id = this.ID.replace(' ', '_').toUpperCase();
			conf.postion_x = OptionalInt.of(this.POSITION_X);
			conf.postion_y = OptionalInt.of(this.POSITION_Y);
			return conf;
		}

	}

}
