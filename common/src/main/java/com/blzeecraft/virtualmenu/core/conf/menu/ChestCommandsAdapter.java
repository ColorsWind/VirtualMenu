package com.blzeecraft.virtualmenu.core.conf.menu;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.action.extension.ActionBungeeCord;
import com.blzeecraft.virtualmenu.core.action.extension.ActionCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpenMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionSound;
import com.blzeecraft.virtualmenu.core.action.extension.ActionTell;
import com.blzeecraft.virtualmenu.core.conf.file.FileMapFactory;
import com.blzeecraft.virtualmenu.core.conf.transition.ChestCommandsConf;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

import lombok.val;
import lombok.var;

public class ChestCommandsAdapter {
	public static final LogNode LOG_NODE = LogNode.of("ChestCommandsAdapter");

	public static PacketMenu parse(File file) throws IOException {
		val map = FileMapFactory.read(LOG_NODE, file);
		val node = LogNode.of(String.join("|", "cc", FileMapFactory.getFileNameNoEx(file)));
		val conf = load(node, map);
		val menu = ConfMenuFactory.convert(node, conf);
		return menu;
	}

	public static StandardConf load(LogNode node, Map<String, Object> map) {
		return new ChestCommandsConf(node, map).remap();
	}

	public static String remap(String name) {
		int index = name.indexOf(':');
		if (index < 0 || index >= name.length()) {
			return ActionCommand.remap(name);
		}
		val key = name.substring(0, index).toLowerCase();
		var value = name.substring(index + 1).replace("%player%", "<player>");
		if (value.startsWith(" ")) {
			value = value.substring(1);
		}
		switch (key) {
		case "console":
			return ActionConsoleCommand.remap(value);
		case "op":
			return ActionOpCommand.remap(value);
		case "open":
		case "menu":
			return ActionOpenMenu.remap(value);
		case "server":
			return ActionBungeeCord.remap(value);
		case "tell":
			return ActionTell.remap(value);
		case "sound":
			return ActionSound.remap(value);
		default:
			return "console{command=say 不支持ChestCommands命令类型 " + value + " \\,请检查.}";
		}
	}

}
