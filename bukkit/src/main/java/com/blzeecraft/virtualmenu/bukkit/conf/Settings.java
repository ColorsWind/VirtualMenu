package com.blzeecraft.virtualmenu.bukkit.conf;

import java.io.File;
import java.util.Arrays;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;

public class Settings {
	
	public static boolean useXMaterial = true;
	public static boolean supportNBT = true;
	
	public static void read(VirtualMenuPlugin plugin) {
		File file = new File(plugin.getDataFolder(), "config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		useXMaterial = config.getBoolean("UseXMaterial", true);
		supportNBT = config.getBoolean("SupportNBT", true);
		ConfigurationSection hookSect = config.getConfigurationSection("EconomyHook");
		ConfigurationSection currencySect = config.getConfigurationSection("MultiCurrency");
		Arrays.stream(EconomyPlugins.values()).forEach(type -> {
			type.readIsEnable(hookSect);
			type.readCurrency(currencySect);
		});
		EconomyPlugins.rebuildMapping();
	}

}
