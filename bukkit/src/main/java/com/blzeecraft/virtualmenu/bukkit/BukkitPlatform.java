package com.blzeecraft.virtualmenu.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.core.IPlatformAdapter;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BukkitPlatform implements IPlatformAdapter {
	
	protected final ConcurrentMap<Player, BukkitPlayer> playerMap = new ConcurrentHashMap<>();
	protected final BukkitConsole console = new BukkitConsole(Bukkit.getConsoleSender());
	
	private final VirtualMenuPlugin plugin;

	@Override
	public File getDataFolder() {
		// TODO Auto-generated method stub
		return null;
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
	public AbstractItem<?> emptyItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractItemBuilder<?> createItemBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<IMenuType> getMenuType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMenuType[] getMenuTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IScheduler getScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

}
