package com.blzeecraft.virtualmenu.core.module;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.val;

/**
 * 数据包菜单管理器
 * @author colors_wind
 *
 */
public class PacketManager {
	protected static final PacketManager INSTANCE = new PacketManager();
	
	protected final ConcurrentMap<IUser<?>, IPacketMenu> openMenus;
	protected final PacketHandler packetHandler;

	public PacketManager() {
		openMenus = new ConcurrentHashMap<>();
		packetHandler = new PacketHandler();
	}

	public static Optional<IPacketMenu> getOpenMenu(IUser<?> user) {
		return Optional.ofNullable(INSTANCE.openMenus.get(user));
	}

	public static void closePacketMenu(IUser<?> user) {
		getOpenMenu(user)
				.ifPresent(menu -> closePacketMenu(user, menu));
	}
	
	public static void closePacketMenu(IUser<?> user, IPacketMenu menu) {
		VirtualMenu.createPacketCloseWindow(user, menu.getWindowId()).send();
	}

	public static void openPacketMenu(IUser<?> user, IPacketMenu menu) {
		val open = VirtualMenu.createPacketWindOpen(user, menu.getWindowId(), menu.getType(), menu.getTitle());
		val items = VirtualMenu.createPacketWindowItems(user, menu.getWindowId(), menu.viewItems(user));
		open.send();
		items.send();
	}
	
	protected static ConcurrentMap<IUser<?>, IPacketMenu> getOpenMenus() {
		return INSTANCE.openMenus;
	}
	
	

}
