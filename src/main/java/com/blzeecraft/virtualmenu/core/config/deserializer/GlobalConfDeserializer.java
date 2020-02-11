package com.blzeecraft.virtualmenu.core.config.deserializer;

import com.blzeecraft.virtualmenu.core.config.deserializer.GlobalConfDeserializer.GlobalConf;
import com.blzeecraft.virtualmenu.core.config.node.ObjectNode;

import java.util.Arrays;

import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import lombok.Data;
import lombok.val;

public class GlobalConfDeserializer implements IDeserializer<GlobalConf> {

	@ObjectNode(key = "refresh")
	public int refresh;

	@ObjectNode(key = "title")
	public String title;

	@ObjectNode(key = "type")
	public String type;

	@Override
	public GlobalConf apply(LogNode node) {
		val type = VirtualMenu.getMenuType(type).orElseGet(() -> {
			val sub = node.sub("type");
			PluginLogger.warning(sub, "找不到类型为: " + type + " 类型的菜单.");
			PluginLogger.info(sub, "可用的类型有: " + Arrays.stream(VirtualMenu.getMenuTypes()).map(type -> type.getType()).reduce("无(检查服务端版本)",
					(s1, s2) -> String.join(",", s1, s2)));
			return VirtualMenu.getMenuTypes()[0];
		});
		return new GlobalConf(refresh, title, type);
	}

	@Data
	public class GlobalConf {

		protected final int refresh;
		protected final String title;
		protected final IMenuType type;

	}

}