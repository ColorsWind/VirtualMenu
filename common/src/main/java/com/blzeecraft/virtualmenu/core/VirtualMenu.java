package com.blzeecraft.virtualmenu.core;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.variable.IVariableAdapter;

/**
 * 代表 VirtualMenu 核心, 用来转发对适配器单例的调用.
 * 
 * @author colors_wind
 * @see com.blzeecraft.virtualmenu.core.packet.IPacketAdapter
 * @see IPlatformAdapter
 * @see com.blzeecraft.virtualmenu.core.user.IUser
 */
public class VirtualMenu {
	public static final String PREFIX = "§b[§aVirtual§eMenu§b] ";

	private static IPacketAdapter packetAdapter;
	private static IPlatformAdapter platformAdapter;

	public static void setup(IPacketAdapter packetAdapter) {
		VirtualMenu.packetAdapter = packetAdapter;
		PluginLogger.info(LogNode.ROOT, "使用 PacketAdapter: " + packetAdapter.getVersion());
	}
	
	public static void setup(IPlatformAdapter platformAdapter) {
		VirtualMenu.platformAdapter = platformAdapter;
		PluginLogger.info(LogNode.ROOT, "使用 PlatformAdapter: " + platformAdapter.getVersion());
	}
	
	/**
	 * @return
	 * @see IPlatformAdapter#getUsersOnline()
	 */
	public static Collection<IUser<?>> getUsersOnline() {
		return platformAdapter.getUsersOnline();
	}
	
	/**
	 * @return
	 * @see IPlatformAdapter#getDataFolder()
	 */
	public static File getDataFolder() {
		return platformAdapter.getDataFolder();
	}

	
	/**
	 * @param name
	 * @return
	 * @see IPlatformAdapter#getUser(java.lang.String)
	 */
	public static Optional<IUser<?>> getUser(String name) {
		return platformAdapter.getUser(name);
	}

	/**
	 * @param name
	 * @return
	 * @see IPlatformAdapter#getUserExact(java.lang.String)
	 */
	public static Optional<IUser<?>> getUserExact(String name) {
		return platformAdapter.getUserExact(name);
	}

	/**
	 * @return
	 * @see IPlatformAdapter#getConsole()
	 */
	public static IUser<?> getConsole() {
		return platformAdapter.getConsole();
	}

	/**
	 * @return
	 * @see IPlatformAdapter#emptyItem()
	 */
	public static AbstractItem<?> emptyItem() {
		return platformAdapter.emptyItem();
	}

	/**
	 * @return
	 * @see IPlatformAdapter#createItemBuilder()
	 */
	public static AbstractItemBuilder<?> createItemBuilder() {
		return platformAdapter.createItemBuilder();
	}

	/**
	 * @param name
	 * @return
	 * @see IPlatformAdapter#getMenuType(java.lang.String)
	 */
	public static Optional<IMenuType> getMenuType(String name) {
		return platformAdapter.getMenuType(name);
	}

	/**
	 * @return
	 * @see IPlatformAdapter#getMenuTypes()
	 */
	public static IMenuType[] getMenuTypes() {
		return platformAdapter.getMenuTypes();
	}

	/**
	 * @return
	 * @see IPlatformAdapter#getScheduler()
	 */
	public static IScheduler getScheduler() {
		return platformAdapter.getScheduler();
	}

	/**
	 * @return
	 * @see IPlatformAdapter#getVariableAdapter()
	 */
	public static IVariableAdapter getVariableAdapter() {
		return platformAdapter.getVariableAdapter();
	}
	
	/**
	 * @return
	 * @see IPlatformAdapter#getLogger()
	 */
	public static Logger getLogger() {
		return platformAdapter.getLogger();
	}

	/**
	 * 获取服务端平台适配器.
	 * 
	 * @return 服务端平台适配器.
	 */
	public static IPlatformAdapter getPlatformAdapter() {
		return platformAdapter;
	}

	/**
	 * 获取 Packet 适配器.
	 * 
	 * @return Packet 适配器.
	 */
	public static IPacketAdapter getPacketAdapter() {
		return packetAdapter;
	}




}
