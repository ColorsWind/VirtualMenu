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
import com.blzeecraft.virtualmenu.core.conf.file.FileAndMapFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.MapToConfFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

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
	
	public static void reloadMenu() {
		MENU.clear();
		Arrays.stream(getMenuFolder().listFiles()).filter(FileAndMapFactory::vaildFileType).forEach(file -> {
			try {
				PacketMenu menu = parse(file);
				String name = FileAndMapFactory.getFileNameNoEx(file);
				if (MENU.putIfAbsent(name, menu) != null) {
					PluginLogger.warning(LogNode.of(file.getName()), "已经存在名称为 " + name + " 的菜单, 忽略.");
				}
			} catch (IOException e) {
				PluginLogger.severe(LOG_NODE, "读取 " + file.getName() + " 时发送IO异常.");
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
		Map<String, Object> map = FileAndMapFactory.read(node, file);
		StandardConf conf = MapToConfFactory.read(node, map);
		PacketMenu menu = ConfToMenuFactory.convert(node, conf);
		return menu;
	}

	public static PacketMenu parse(LogNode node, String type, Reader reader) throws IOException {
		Map<String, Object> map = FileAndMapFactory.read(node, type, reader);
		StandardConf conf = MapToConfFactory.read(node, map);
		PacketMenu menu = ConfToMenuFactory.convert(node, conf);
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
