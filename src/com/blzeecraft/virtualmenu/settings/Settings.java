package com.blzeecraft.virtualmenu.settings;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.config.ConfigReader;
import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.IConfig;
import com.blzeecraft.virtualmenu.config.Node;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public class Settings implements IConfig {

	@Node(key = "Prefix", type = DataType.STRING)
	protected String prefix = "§b[§dVirtualMenu§b] ";
	@Node(key = "AntiSpam", type = DataType.INT)
	protected int antiSpam;
	@Node(key = "SupportNBT", type = DataType.BOOLEAN)
	protected boolean supportNBT;
	@Node(key = "Lang.NoEnoughMoney", type = DataType.STRING)
	protected String lang_noEnoughMoney;
	@Node(key = "Lang.NoEnoughPoint", type = DataType.STRING)
	protected String lang_noEnoughPoint;
	@Node(key = "Lang.NoEnoughtItem", type = DataType.STRING)
	protected String lang_noEnoughtItem;
	@Node(key = "Lang.NoPermission", type = DataType.STRING)
	protected String lang_noPermission;
	@Node(key = "Lang.InternalError", type = DataType.STRING)
	protected String lang_internalError;
	
	@Getter
	protected static Settings instance;
	
	public static Settings init(VirtualMenuPlugin pl) {
		return instance = new Settings(pl);
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(new StringBuilder(instance.prefix).append(" ").append(message).toString());
	}
	
	private VirtualMenuPlugin pl;
	
	public Settings(VirtualMenuPlugin pl) {
		this.pl = pl;
	}
	
	public void readSetting() {
		File file = pl.getFileManager().configFile;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigReader.read(Settings.class, this, config);
	}

	@Override
	public String getLogPrefix() {
		return "config";
	}

	@Override
	public void apply() {
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
	}
	
	

}
