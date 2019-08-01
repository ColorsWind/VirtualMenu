package com.blzeecraft.virtualmenu.bound;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;

import com.blzeecraft.virtualmenu.InsensitiveMap;
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
	protected final InsensitiveMap<ChestMenu> menus;
	protected final BoundHandler handler;
	
	public BoundManager(VirtualMenuPlugin pl) {
		this.pl = pl;
		this.menus = new InsensitiveMap<>();
		this.bounds = new EnumMap<>(Material.class);
		this.handler = new BoundHandler(this);
	}
	
	public void readBoundAction() {
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
	
	public void registerListener() {
		Bukkit.getPluginManager().registerEvents(handler, pl);
	}
	

	
	public void addBound(List<String> boundCommand, ChestMenu chestMenu) {
		for(String cmd : boundCommand) {
			ChestMenu menu = menus.putIfAbsent(cmd, chestMenu);
			if(menu != null) {
				throw new IllegalArgumentException(menu.getName() + "已经注册了命令:" + cmd);
			}
		}
		
	}
	
	public ChestMenu getByAction(Material type, Action action) {
		Map<Action, ChestMenu> map = bounds.get(type);
		if (map != null) {
			return map.get(action);
		}
		return null;
	}
	
	public ChestMenu getByCommand(String cmd) {
		return menus.get(cmd);
	}
	

	@Override
	public String getLogPrefix() {
			return "#Bound";
	}

	public void unregister() {
		HandlerList.unregisterAll(handler);
		menus.clear();
		bounds.clear();
		
	}


}
