package com.blzeecraft.virtualmenu;

import org.bukkit.Bukkit;
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
import com.blzeecraft.virtualmenu.net.Metrics;
import com.blzeecraft.virtualmenu.net.UpdateChecker;
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
	protected UpdateChecker updateChecker;
	protected Metrics metrics;
	
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
		boundManager = BoundManager.init(this);
		menuManager = MenuManager.init(this);
		menuManager.readMenu();
		boundManager.readBoundAction();
		boundManager.registerListener();
		packetManager = PacketManager.init(this);
		packetManager.registerListener();
		menuBuilder = MenuBuilder.init(this);
		menuBuilder.register();
		commandManager = CommandManager.init(this);
		commandManager.registerCommands();
		commandManager.registerHandlers();
		updateChecker = new UpdateChecker(this);
		updateChecker.startTask();
		updateChecker.register();
		metrics = new Metrics(this);
		if (metrics.isEnabled()) {
			this.getLogger().info("使用bstats统计插件使用情况");
		} else {
			this.getLogger().info("为支持作者,请不要关闭bstats统计");
		}
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
	
	public void runOnPrimaryThread(Runnable task) {
		if (Bukkit.isPrimaryThread()) {
			task.run();
		} else {
			Bukkit.getScheduler().runTask(this, new Runnable() {

				@Override
				public void run() {
					task.run();

				}
			});
		}
	}



}
