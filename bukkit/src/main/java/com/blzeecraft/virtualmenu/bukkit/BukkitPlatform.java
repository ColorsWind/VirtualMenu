package com.blzeecraft.virtualmenu.bukkit;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.blzeecraft.virtualmenu.bukkit.item.BukkitItemBuilder;
import com.blzeecraft.virtualmenu.core.IPlatformAdapter;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BukkitPlatform implements IPlatformAdapter, Listener {
	
	private final VirtualMenuPlugin plugin;
	protected final ConcurrentMap<Player, WrapPlayerBukkit> playerMap;
	protected final WrapConsoleBukkit console;
	protected final WrapSchedulerBukkit scheduler;

	public BukkitPlatform(VirtualMenuPlugin plugin) {
		this.plugin = plugin;
		playerMap = new ConcurrentHashMap<>();
		console = new WrapConsoleBukkit(Bukkit.getConsoleSender());
		scheduler = new WrapSchedulerBukkit(plugin);
	}
	
	public void registerEvent() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void handle(PlayerJoinEvent e) {
		WrapPlayerBukkit user = new WrapPlayerBukkit(plugin, e.getPlayer());
		playerMap.put(e.getPlayer(), user);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void handle(PlayerQuitEvent e) {
		playerMap.remove(e.getPlayer());
	}
	
	@Override
	public File getDataFolder() {
		return plugin.getDataFolder();
	}

	@Override
	public Collection<IUser<?>> getUsersOnline() {
		return Collections.unmodifiableCollection(playerMap.values());
	}

	@Override
	public Optional<IUser<?>> getUser(String name) {
		return getUserExact(Bukkit.getPlayer(name));
	}

	@Override
	public Optional<IUser<?>> getUserExact(String name) {
		return getUserExact(Bukkit.getPlayerExact(name));
	}
	
	public Optional<IUser<?>> getUserExact(Player player) {
		return Optional.ofNullable(playerMap.get(player));
	}

	@Override
	public IUser<?> getConsole() {
		return console;
	}

	@Override
	public WrapItemBukkit emptyItem() {
		return WrapItemBukkit.EMPTY_ITEM;
	}

	@Override
	public BukkitItemBuilder createItemBuilder() {
		return new BukkitItemBuilder();
	}

	@Override
	public Optional<IMenuType> getMenuType(String name) {
		return Optional.ofNullable(MenuType.valueOf(name.toUpperCase()));
	}

	@Override
	public IMenuType[] getMenuTypes() {
		return MenuType.values();
	}

	@Override
	public IScheduler getScheduler() {
		return scheduler;
	}
	
	@Override
	public String getVersion() {
		return "BukkitPlatform";
	}
	
	@SuppressWarnings("unchecked")
	public Collection<? extends Player> getPlayerOnline() {
		try {
			Method method = Bukkit.class.getMethod("getOnlinePlayers");
			if (method.getReturnType().isArray()) {
				return Arrays.asList((Player[]) method.invoke(null));
			} else {
				return ((Collection<? extends Player>) method.invoke(null));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

}
