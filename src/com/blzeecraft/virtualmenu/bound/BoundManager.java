package com.blzeecraft.virtualmenu.bound;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;
import com.blzeecraft.virtualmenu.menu.ChestMenu;

import lombok.Getter;

public class BoundManager implements ILog {
	@Getter
	private static BoundManager instance;

	public static BoundManager init(VirtualMenuPlugin pl) {
		return instance = new BoundManager(pl);
	}
	
	protected final VirtualMenuPlugin pl;
	protected final Map<Material, Map<Action, ChestMenu>> bounds;
	protected final BoundHandler handler;
	
	public BoundManager(VirtualMenuPlugin pl) {
		this.pl = pl;
		this.bounds = new EnumMap<>(Material.class);
		this.handler = new BoundHandler(this);
	}
	
	public void read() {
		FileConfiguration boundConfig = YamlConfiguration.loadConfiguration(pl.getFileManager().getBoundFile());
		bounds.clear();
		l1:for(String path : boundConfig.getKeys(false)) {
			ConfigurationSection cs = boundConfig.getConfigurationSection(path);
			Map<Action, ChestMenu> map = new EnumMap<>(Action.class);
			Material material = null;
			try {
				material = Material.valueOf(path);
			} catch (IllegalArgumentException e) {
				PluginLogger.severe(this, "Expect: [Material] Set: " + path + "(INVALID Material)");
				continue l1;
			}
			bounds.put(material, map);
			l2:for(String sub : cs.getKeys(false)) {
				Action action = null;
				try {
					action = Action.valueOf(sub);
				} catch (IllegalArgumentException e) {
					PluginLogger.severe(this, path, "Expect: [Action] Set: " + path + "(INVALID Action)");
					continue l2;
				}
				ChestMenu menu = pl.getMenuManager().getMenu(cs.getString(sub));
				if (menu == null) {
					PluginLogger.severe(this, path, "Expect: [Menu] Set: " + path + "(INVALID Menu)");
					continue l2;
				}
				map.put(action, menu);
				
			}
		}
	}
	
	public void register() {
		Bukkit.getPluginManager().registerEvents(handler, pl);
	}
	
	public ChestMenu get(Material type, Action action) {
		Map<Action, ChestMenu> map = bounds.get(type);
		if (map != null) {
			return map.get(action);
		}
		return null;
	}
	
	public Map<Action, ChestMenu> get(Material m) {
		return bounds.get(m);
	}

	@Override
	public String getLogPrefix() {
			return "#Bound";
	}

}
