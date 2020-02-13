package com.blzeecraft.virtualmenu.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.icon.EmptyIcon;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 用于保存玩家目前打开菜单的相关信息. 应在确认玩家打开菜单请求后, 创建发送给客户端的 Packet 前, 由 {@link PacketManager}
 * 创建. 玩家每次打开菜单都需要创建一个新的 {@link UserSession}}
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
@ToString
@EqualsAndHashCode
public class UserSession {
	public static final EmptyIcon EMPTY_ICON = new EmptyIcon();
	public static final AbstractItem<?> EMPTY_ITEM = VirtualMenu.emptyItem();

	@Getter
	@EqualsAndHashCode.Include
	protected final IUser<?> user;
	
	@Getter
	@EqualsAndHashCode.Include
	protected final IPacketMenu menu;

	protected final ConcurrentMap<Icon, AbstractItem<?>> viewItem;
	protected final ConcurrentMap<MultiIcon, Icon> viewIcon;
	protected final AtomicReferenceArray<AbstractItem<?>> fastAccess;

	/**
	 * 创建一个新的 UserSession 对象, 创建对象后还需要进行初始化 {@link #init(Icon[])}.
	 * 
	 * @param user
	 * @param menu
	 */
	public UserSession(IUser<?> user, IPacketMenu menu) {
		this.user = user;
		this.menu = menu;
		this.viewItem = new ConcurrentHashMap<>();
		this.viewIcon = new ConcurrentHashMap<>();
		this.fastAccess = new AtomicReferenceArray<AbstractItem<?>>(menu.getSize());
	}

	/**
	 * 获取对应 Icon 缓存的 Item. 保证返回可用的结果.
	 * 
	 * @param icon
	 * @return 缓存的 Item.
	 */
	public AbstractItem<?> getCacheItem(Icon icon) {
		return viewItem.getOrDefault(icon, EMPTY_ITEM);
	}

	/**
	 * 获取对应 MultiIcon 缓存的 Icon. 保证返回可用的结果.
	 * 
	 * @param icon
	 * @return 缓存的 Icon.
	 */
	public Icon getCacheIcon(MultiIcon icon) {
		return viewIcon.getOrDefault(icon, EMPTY_ICON);
	}

	/**
	 * 获取对应 MultiIcon 缓存的 Icon. 保证返回可用的结果.
	 * 
	 * @param slot
	 * @return 缓存的 Item.
	 */
	public AbstractItem<?> getCacheItem(int slot) {
		return fastAccess.get(slot);
	}

	/**
	 * 刷新指定 slot 的 Icon. 保证返回可用的结果.
	 * 
	 * @param slot
	 * @param icon
	 * @return 新的 Item
	 */
	public AbstractItem<?> updateItem(int slot, Icon icon) {
		AbstractItem<?> item = icon.refreshItem(this);
		fastAccess.set(slot, item);
		return item;
	}

	/**
	 * 初始化 UserSession.
	 * 
	 * @param icons 菜单包含的 Icon, 必须保证数组合法(数组大小等于菜单容量且所有元素非空).
	 * @return
	 */
	public AbstractItem<?>[] init(Icon[] icons) {
		int length = icons.length;
		AbstractItem<?>[] items = new AbstractItem<?>[length];
		for (int i = 0; i < length; i++) {
			Icon icon = icons[i];
			AbstractItem<?> item;
			if (icon instanceof MultiIcon) {
				Icon realIcon = ((MultiIcon) icon).viewIcon(this);
				item = realIcon.refreshItem(this);
				viewIcon.put((MultiIcon) icon, realIcon);
				viewItem.put(realIcon, item);
			} else {
				item = icon.refreshItem(this);
				viewItem.put(icon, item);
			}
			fastAccess.set(i, item);
			items[i] = item;
		}
		return items;
	}

}
