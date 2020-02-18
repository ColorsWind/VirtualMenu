package com.blzeecraft.virtualmenu.core;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;
import com.blzeecraft.virtualmenu.core.user.IUser;

/**
 * 服务端平台适配器
 * @author colors_wind
 *
 */
public interface IPlatformAdapter {
	
	/**
	 * 获取插件的数据目录.
	 * @return 数据目录
	 */
	File getDataFolder();
	
	/**
	 * 获取当前在线的所有玩家(不包括控制台).
	 * @return 在线的玩家
	 */
	Collection<IUser<?>> getUsersOnline();
	
	/**
	 * 通过用户名获取在线的玩家.
	 * @param name
	 * @return 玩家,如果找不到玩家,返回 {@link Optional#empty()}.
	 */
	Optional<IUser<?>> getUser(String name);
	
	/**
	 * 通过玩家名称精确地获取在线的玩家.
	 * @param name 玩家名称
	 * @return 玩家,如果找不到玩家,返回 {@link Optional#empty()}.
	 */
	Optional<IUser<?>> getUserExact(String name);
	
	/**
	 * 获取控制台对象
	 * @return
	 */
	IUser<?> getConsole();

	/**
	 * 获取 Item(Material = AIR).
	 * @param <T>
	 * @return Item
	 */
	AbstractItem<?> emptyItem();
	
	/**
	 * 创建一个新的 ItemBuilder.
	 * @return ItemBuilder
	 */
	AbstractItemBuilder<?> createItemBuilder();
	
	/**
	 * 通过菜单类型的名称获取 MenuType.
	 * @param name
	 * @return MenuType, 如果找不到返回 {@link Optional#empty()}.
	 */
	Optional<IMenuType> getMenuType(String name);

	/**
	 * 获取所有可用的 MenuType.
	 * @return 可用的 MenuType.
	 */
	IMenuType[] getMenuTypes();

	/**
	 * 获取任务调度器 Scheduler.
	 * @return Scheduler
	 */
	IScheduler getScheduler();

	
	



	
	
}
