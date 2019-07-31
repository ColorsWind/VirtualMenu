package com.blzeecraft.virtualmenu.menu;

import java.io.File;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blzeecraft.virtualmenu.InsensitiveMap;
import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.config.ConfigReader;
import com.blzeecraft.virtualmenu.menu.iiem.ExtendedIcon;

import lombok.Getter;

public class MenuManager {
	@Getter
	private static MenuManager instance;
	
	public static MenuManager init(VirtualMenuPlugin pl) {
		return instance = new MenuManager(pl);
	}
	
	
	//start
	private final Map<String, ChestMenu> menus;
	private final VirtualMenuPlugin pl;
	
	private MenuManager(VirtualMenuPlugin pl) {
		menus = InsensitiveMap.newInsensitiveMap();
		this.pl = pl;
	}
	
	
	public void readMenu() {
		menus.clear();
		File folder = new File(pl.getDataFolder(), "menu");
		for(File file : folder.listFiles()) {
			String name = file.getName();
			if (name.endsWith(".yml")) {
				name = name.substring(0, name.length() - 4);
				ChestMenu menu = readChestMenu(name, file);
				menus.put(name, menu);
			}
		}
	}
	
	@lombok.SneakyThrows
	public ChestMenu readChestMenu(String name, File file) {
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		ChestMenu menu = new ChestMenu(name);
		for(String path : yaml.getKeys(false)) {
			ConfigurationSection sect = yaml.getConfigurationSection(path);
			if ("menu-settings".equals(path)) {
				ConfigReader.read(ChestMenu.class, menu, sect);
				menu.applyColor();
			} else {
				ExtendedIcon icon = new ExtendedIcon(menu, path);
				ConfigReader.read(ExtendedIcon.class, icon, sect);
				menu.addIcon(icon);
			}
		}
		return menu;
	}
	

	public ChestMenu getMenu(String name) {
		return menus.get(name);
	}

}
