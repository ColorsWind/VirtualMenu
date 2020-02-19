package com.blzeecraft.virtualmenu.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bukkit.item.BukkitItemBuilder;
import com.blzeecraft.virtualmenu.core.IPlatformAdapter;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BukkitPlatform implements IPlatformAdapter {
	
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
	public BukkitItem emptyItem() {
		return BukkitItem.EMPTY_ITEM;
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

}
