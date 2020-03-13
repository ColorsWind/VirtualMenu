package com.blzeecraft.virtualmenu.core.conf.transition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionBungeeCord;
import com.blzeecraft.virtualmenu.core.action.extension.ActionCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpenMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionSound;
import com.blzeecraft.virtualmenu.core.action.extension.ActionTell;
import com.blzeecraft.virtualmenu.core.conf.file.FileMapFactory;
import com.blzeecraft.virtualmenu.core.conf.menu.ConfMenuFactory;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

import lombok.val;
import lombok.var;

public class ChestCommandsAdapter {
	public static final LogNode LOG_NODE = LogNode.of("ChestCommandsAdapter");

	public static PacketMenu parse(File file) throws IOException {
		val map = FileMapFactory.read(LOG_NODE, file);
		val node = LogNode.of(String.join("|", "cc", FileMapFactory.getFileNameNoEx(file)));
		val conf = load(node, map);
		try {
			updateFile(file, conf);
			PluginLogger.info(node, "转换为 VirtualMenu 格式.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		val menu = ConfMenuFactory.convert(node, conf);
		return menu;
	}

	public static void updateFile(File file, StandardConf conf) throws IOException {
		val updateFile = new File(file.getParentFile(), file.getName() + ".update");
		if (updateFile.exists()) {
			updateFile.delete();
		}
		val map = conf.serialize();
		FileMapFactory.getFileFormat("yaml").write(LOG_NODE, updateFile, map);
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
	public static File getChestCommandsFolder() {
		val file = new File(VirtualMenu.getDataFolder(), "chestcommands");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public static Map<String, PacketMenu> loadAll() {
		val menuMap = new HashMap<String, PacketMenu>();
		Arrays.stream(getChestCommandsFolder().listFiles()).filter(FileMapFactory::vaildFileType).forEach(file -> {
			try {
				val name = FileMapFactory.getFileNameNoEx(file);
				val menu = parse(file);
				menuMap.put(name, menu);
			} catch (Exception e) {
				PluginLogger.severe(LOG_NODE, "处理 ChestCommands 格式文件 " + file.getName() + " 时发生异常.");
				e.printStackTrace();
			}
		});
		return menuMap;
	}

}
