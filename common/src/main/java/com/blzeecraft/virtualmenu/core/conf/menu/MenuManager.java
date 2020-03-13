package com.blzeecraft.virtualmenu.core.conf.menu;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
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
import com.blzeecraft.virtualmenu.core.conf.transition.ChestCommandsAdapter;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;
import com.blzeecraft.virtualmenu.core.user.UserManager;

import lombok.NonNull;
import lombok.val;

/**
 * 有关菜单配置读取/保存的逻辑.
 * @author colors_wind
 *
 */
public class MenuManager {

	private static final ConcurrentMap<String, PacketMenu> MENU = new ConcurrentHashMap<>();
	public static final LogNode LOG_NODE = LogNode.of("#MenuManager");
	
	public static File getMenuFolder() {
		File file = new File(VirtualMenu.getDataFolder(), "menu");
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	

	
	public static PacketMenu addMenuIfAbsent(@NonNull String name,@NonNull  PacketMenu menu) {
		return MENU.putIfAbsent(name, menu);
	}
	
	public static PacketMenu addMenu(@NonNull String name,@NonNull  PacketMenu menu) {
		return MENU.put(name, menu);
	}
	
	public static PacketMenu removeMenu(@NonNull String name) {
		val menu = MENU.get(name);
		if (menu == null) {
			return null;
		}
		val copy = new ArrayList<>(menu.getSessions());
		copy.forEach(UserManager::closePacketMenu);
		MENU.remove(name);
		return menu;
	}
	
	private static void putMenu(@NonNull String name, @NonNull PacketMenu menu) {
		if (addMenuIfAbsent(name, menu) != null) {
			PluginLogger.warning(LogNode.of(name), "已经存在名称为 " + name + " 的菜单, 忽略.");
		}
	}
	
	private static void putMenu(@NonNull Map<String, PacketMenu> map) {
		map.forEach((k,v) -> putMenu(k,v));
	}
	
	public static void reloadMenu() {
		UserManager.closeAllMenu();
		MENU.clear();
		Arrays.stream(getMenuFolder().listFiles()).filter(FileMapFactory::vaildFileType).forEach(file -> {
			try {
				PacketMenu menu = parse(file);
				String name = FileMapFactory.getFileNameNoEx(file);
				putMenu(name, menu);
			} catch (Exception e) {
				PluginLogger.severe(LOG_NODE, "读取 " + file.getName() + " 时发生异常.");
				e.printStackTrace();
			}
		});
		val ccMenus = ChestCommandsAdapter.loadAll();
		putMenu(ccMenus);
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
