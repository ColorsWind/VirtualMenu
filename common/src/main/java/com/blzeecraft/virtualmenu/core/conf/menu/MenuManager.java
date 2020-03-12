package com.blzeecraft.virtualmenu.core.conf.menu;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.conf.convert.ConvertFunctions;
import com.blzeecraft.virtualmenu.core.conf.file.FileMapFactory;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

import lombok.val;

/**
 * 有关菜单配置读取/保存的逻辑.
 * @author colors_wind
 *
 */
public class MenuManager {

	public static final ConcurrentMap<String, PacketMenu> MENU = new ConcurrentHashMap<>();
	public static final LogNode LOG_NODE = LogNode.of("#MenuManager");
	
	public static File getMenuFolder() {
		File file = new File(VirtualMenu.getDataFolder(), "menu");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	public static File getChestCommandsFolder() {
		val file = new File(VirtualMenu.getDataFolder(), "chestcommands");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	public static void reloadMenu() {
		MENU.clear();
		Arrays.stream(getMenuFolder().listFiles()).filter(FileMapFactory::vaildFileType).forEach(file -> {
			try {
				PacketMenu menu = parse(file);
				String name = FileMapFactory.getFileNameNoEx(file);
				if (MENU.putIfAbsent(name, menu) != null) {
					PluginLogger.warning(LogNode.of(file.getName()), "已经存在名称为 " + name + " 的菜单, 忽略.");
				}
			} catch (IOException e) {
				PluginLogger.severe(LOG_NODE, "读取 " + file.getName() + " 时发送IO异常.");
				e.printStackTrace();
			}
		});
		Arrays.stream(getChestCommandsFolder().listFiles()).filter(FileMapFactory::vaildFileType).forEach(file -> {
			try {
				PacketMenu menu = ChestCommandsAdapter.parse(file);
				String name = FileMapFactory.getFileNameNoEx(file);
				if (MENU.putIfAbsent(name, menu) != null) {
					PluginLogger.warning(LogNode.of(file.getName()), "已经存在名称为 " + name + " 的菜单, 忽略.");
				}
			} catch (IOException e) {
				PluginLogger.severe(LOG_NODE, "读取ChestCommands格式文件 " + file.getName() + " 时发送IO异常.");
				e.printStackTrace();
			}
		});
		PluginLogger.info(LOG_NODE, "读取菜单完成, 已加载 " + MENU.size() + " 个菜单.");
	}
	
	public static Optional<PacketMenu> getMenu(String name) {
		return Optional.ofNullable(MENU.get(name));
	}
	
	public static PacketMenu parse(File file) throws IOException {
		LogNode node = LogNode.of(file.getName());
		Map<String, Object> map = FileMapFactory.read(node, file);
		StandardConf conf = (StandardConf) ConvertFunctions.convertObject(map, StandardConf.class);
		PacketMenu menu = ConfMenuFactory.convert(node, conf);
		return menu;
	}
	

	public static PacketMenu parse(LogNode node, String type, Reader reader) throws IOException {
		Map<String, Object> map = FileMapFactory.read(node, type, reader);
		StandardConf conf = (StandardConf) ConvertFunctions.convertObject(map, StandardConf.class);
		PacketMenu menu = ConfMenuFactory.convert(node, conf);
		return menu;
		
	}

	public static Collection<PacketMenu> getMenus() {
		return MENU.values();
	}
	
	public static Set<Entry<String, PacketMenu>> getMenuAndNames() {
		return MENU.entrySet();
	}
	
	public static Set<String> getMenusName() {
		return MENU.keySet();
	}

}
