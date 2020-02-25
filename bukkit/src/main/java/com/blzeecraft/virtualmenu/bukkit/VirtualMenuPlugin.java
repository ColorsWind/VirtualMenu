package com.blzeecraft.virtualmenu.bukkit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.blzeecraft.virtualmenu.bukkit.conf.EconomyPlugins;
import com.blzeecraft.virtualmenu.bukkit.conf.Settings;
import com.blzeecraft.virtualmenu.bukkit.economy.IEconomyHook;
import com.blzeecraft.virtualmenu.bukkit.packet.ProtocolLibAdapter;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.conf.menu.MenuManager;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class VirtualMenuPlugin extends JavaPlugin {

	@Getter
	private static VirtualMenuPlugin instance;
	private ProtocolLibAdapter packetAdapter;
	private BukkitPlatform platformAdapter;

	@Override
	public void onEnable() {
		instance = this;
		registerAdapter();
		generate();
		PluginLogger.setup();
		Settings.read(this);
		registerCommand();
		handleReload();
		// should load after the server started
		Bukkit.getScheduler().runTask(this, () -> {
			MenuManager.reloadMenu();
			PluginLogger.info(LogNode.ROOT, "成功加载: " + MenuManager.getMenus().size() + " 个菜单");
		});
	}
	
	@Override
	public void onDisable() {
		PacketManager.closeAllMenu();
	}

	public void handleReload() {
		// fix reload issue
		platformAdapter.getPlayerOnline().stream().map(player -> new WrapPlayerBukkit(this, player))
				.forEach(user -> platformAdapter.playerMap.put(user.getHandle(), user));
		;

	}

	public void registerCommand() {
		BukkitCommandHandler handler = new BukkitCommandHandler(this);
		this.getCommand("virtualmenu").setExecutor(handler);
	}

	public void registerAdapter() {
		// register adapter
		platformAdapter = new BukkitPlatform(this);
		platformAdapter.registerEvent();
		VirtualMenu.setup(platformAdapter);
		packetAdapter = new ProtocolLibAdapter(this);
		packetAdapter.registerEvent();
		VirtualMenu.setup(packetAdapter);
	}

	@SneakyThrows
	public void generate() {
		// generate resource
		File menuFolder = new File(getDataFolder(), "menu");
		if (!menuFolder.exists()) {
			menuFolder.mkdirs();
		}
		File logFolder = new File(getDataFolder(), "logs");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}
		File confFile = new File(getDataFolder(), "config.yml");
		if (!confFile.exists()) {
			this.saveDefaultConfig();
		}
		File exampleFile = new File(menuFolder, "example.yml");
		if (!exampleFile.exists()) {
			BufferedInputStream in = new BufferedInputStream(this.getResource("example.yml"));
			FileOutputStream out = new FileOutputStream(exampleFile);
			byte[] buff = new byte[1024];
			int len;
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		}
	}

	public Optional<IEconomyHook> getEconomy(String currency) {
		Optional<IEconomyHook> opt = EconomyPlugins.getHandlerByCurrency(currency);
		if (!opt.isPresent()) {
			PluginLogger.warning(LogNode.ROOT, "尝试获取未定义的货币: " + currency);
		}
		return opt;
	}

}
