package com.blzeecraft.virtualmenu;

import org.bukkit.plugin.java.JavaPlugin;

import com.blzeecraft.virtualmenu.action.ActionManager;
import com.blzeecraft.virtualmenu.bound.BoundManager;
import com.blzeecraft.virtualmenu.bridge.EconomyBridge;
import com.blzeecraft.virtualmenu.bridge.PlayerPointsBridge;
import com.blzeecraft.virtualmenu.builder.MenuBuilder;
import com.blzeecraft.virtualmenu.command.CommandManager;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;
import com.blzeecraft.virtualmenu.menu.MenuManager;
import com.blzeecraft.virtualmenu.packet.PacketManager;
import com.blzeecraft.virtualmenu.settings.FileManager;
import com.blzeecraft.virtualmenu.settings.Settings;

import lombok.Getter;

@Getter
public class VirtualMenuPlugin extends JavaPlugin implements ILog {
	@Getter
	protected static VirtualMenuPlugin instance;
	
	protected MenuManager menuManager;
	protected PacketManager packetManager;
	protected CommandManager commandManager;
	protected ActionManager actionManager;
	protected PluginLogger pluginLogger;
	protected FileManager fileManager;
	protected Settings settings;
	protected BoundManager boundManager;
	protected MenuBuilder menuBuilder;

	
	@Override
	public void onEnable() {
		instance = this;
		pluginLogger = PluginLogger.init(this);
		fileManager = FileManager.init(this);
		fileManager.generate();
		pluginLogger.addFileHandler();
		settings = Settings.init(this);
		settings.readSetting();
		if (EconomyBridge.setupEconomy()) {
			this.getLogger().info("找到经济插件");
		}
		if (PlayerPointsBridge.setupPlugin()) {
			this.getLogger().info("找到PlayerPoints");
		}
		if (settings.isSupportNBT()) {
			this.getLogger().info("启用NBT支持");
		}
		actionManager = ActionManager.init();
		actionManager.registerActions();
		menuManager = MenuManager.init(this);
		menuManager.readMenu();
		boundManager = BoundManager.init(this);
		boundManager.read();
		boundManager.register();
		packetManager = PacketManager.init(this);
		packetManager.registerListener();
		menuBuilder = MenuBuilder.init(this);
		menuBuilder.register();
		commandManager = CommandManager.init(this);
		commandManager.registerCommands();
		commandManager.registerHandlers();
	}
	
	@Override
	public void onDisable() {
		menuBuilder.closeAll();
		packetManager.unregisterHandler();
		pluginLogger.unregister();
		
	}
	
	

	@Override
	public String getLogPrefix() {
		return "#root";
	}



}
