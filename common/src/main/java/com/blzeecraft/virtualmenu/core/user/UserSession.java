package com.blzeecraft.virtualmenu.core.user;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutSetSlot;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;

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
	public static final LogNode LOG_NODE = LogNode.of("#UserSession");
	public static final AbstractItem<?> EMPTY_ITEM = VirtualMenu.emptyItem();
	public static final Object EMPTY_RAW_ITEM = EMPTY_ITEM.getHandle();
	public static final Class<?> RAW_ITEM_TYPE = EMPTY_RAW_ITEM.getClass();
	private static Object ensureNotNull(Object item) {
		return item == null ? EMPTY_RAW_ITEM : item;
	}

	@Getter
	@EqualsAndHashCode.Include
	protected final IUser<?> user;
	
	@Getter
	@EqualsAndHashCode.Include
	protected final IPacketMenu menu;
	
	protected final IPacketAdapter packetAdapter;
	
	
	// 保存玩家 PacketMenu 的 RawItem
	protected final AtomicReferenceArray<Object> packetMenuRawItem;
	// 保存玩家 Inventory 的 RawItem
	protected final AtomicReferenceArray<Object> inventoryRawItem;
	// 保存玩家 MultiIcon 显示的 Icon
	protected final ConcurrentMap<MultiIcon, Icon> viewIcon;

	/**
	 * 创建一个新的 UserSession 对象, 创建对象后还需要进行初始化 {@link #init(Icon[])}.
	 * 
	 * @param user
	 * @param menu
	 */
	public UserSession(IUser<?> user, IPacketMenu menu) {
		this.user = user;
		this.menu = menu;
		this.packetMenuRawItem = new AtomicReferenceArray<>(menu.getSize());
		this.inventoryRawItem = new AtomicReferenceArray<>(user.getInventoryRawItems());
		this.viewIcon = new ConcurrentHashMap<>();
		this.packetAdapter = VirtualMenu.getPacketAdapter();
	}
	
	public UserSession init() {
		for(int i=0;i<this.inventoryRawItem.length();i++) {
			this.inventoryRawItem.getAndUpdate(i, UserSession::ensureNotNull);
		}
		AbstractItem<?>[] packetMenuItems = menu.viewItems(this);
		for(int i=0;i<this.packetMenuRawItem.length();i++) {
			this.packetMenuRawItem.set(i, packetMenuItems[i].getHandle());
		}
		return this;
	}


	
	/**
	 * 获取 Inventory 缓存的 RawItem.
	 * 
	 * @param slot
	 * @return 缓存的 RawItem. 如果获取失败, 返回 {@link #EMPTY_RAW_ITEM}.
	 */
	public Object getCacheInventoryRawItem(int slot) {
		this.checkBoundsInventory(slot);
		return inventoryRawItem.get(slot);
	}
	
	
	/**
	 * 获取 Inventory 缓存的所有 RawItem.
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getCacheInventoryRawItems() {
		Object[] itemArray = (Object[]) Array.newInstance(RAW_ITEM_TYPE, inventoryRawItem.length());
		for(int i=0;i<this.inventoryRawItem.length();i++) {
			itemArray[i] = this.inventoryRawItem.get(i);
		}
		return (T[]) itemArray;
	}
	
	/**
	 * 更新 Inventory 缓存的 RawItem.
	 * @param slot
	 * @param item
	 */
	public void updateCacheInventoryRawItem(int slot, Object item) {
		this.checkBoundsInventory(slot);
		this.inventoryRawItem.set(slot, ensureNotNull(item));
	}
	
	/**
	 * 更新 Inventory 缓存的所有 RawItem.
	 * @param <T>
	 * @param array
	 */
	public void updateCacheInventoryRawItems(Object[] array) {
		this.checkSizeInventory(array);
		for(int i=0;i<this.inventoryRawItem.length();i++) {
			this.inventoryRawItem.set(i, ensureNotNull(array[i]));
		}
	}
	
	private void checkBoundsInventory(int slot) {
		if (slot < 0 || slot >= inventoryRawItem.length()) {
			PluginLogger.warning(LOG_NODE, "尝试获取 Inventory 缓存时发生错误: slot=" + slot + ", inventorySize" + inventoryRawItem.length());
			throw new IndexOutOfBoundsException();
		}
	}
	
	private void checkSizeInventory(Object[] array) {
		if (array.length != this.inventoryRawItem.length()) {
			PluginLogger.warning(LOG_NODE, "访问 Inventory 缓存时发生错误: inventorySize=" + this.inventoryRawItem.length() + ", parameterize=" + array.length);
			throw new IndexOutOfBoundsException();
		}
	}




	
	/**
	 * 获取 PacketMenu 缓存的 RawItem
	 * @param slot
	 * @return 缓存的 RawItem.
	 */
	public Object getCachePacketMenuRawItem(int slot) {
		this.checkBoundsPacketMenu(slot);
		return packetMenuRawItem.get(slot);
	}
	
	/**
	 * 获取 PacketMenu 缓存的所有 RawItem.
	 * @param <T>
	 * @param array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getCachePacketMenuRawItems() {
		Object[] itemArray = (Object[]) Array.newInstance(RAW_ITEM_TYPE, inventoryRawItem.length());
		for(int i=0;i<this.packetMenuRawItem.length();i++) {
			itemArray[i] = this.packetMenuRawItem.get(i);
		}
		return (T[]) itemArray;
	}
	
	/**
	 * 更新 Inventory 缓存的 RawItem.
	 * @param slot
	 * @param item
	 */
	public void updateCachePacketMenuRawItem(int slot, Object item) {
		this.checkBoundsPacketMenu(slot);
		this.packetMenuRawItem.set(slot, ensureNotNull(item));
	}
	

	/**
	 * 更新 Inventory 缓存的所有 RawItem.
	 * @param array
	 */
	public void updateCachePacketMenuRawItems(Object[] array) {
		this.checkSizePacketMenu(array);
		for(int i=0;i<this.packetMenuRawItem.length();i++) {
			this.packetMenuRawItem.set(i, ensureNotNull(array[i]));
		}
	}
	
	
	private void checkBoundsPacketMenu(int slot) {
		if (slot < 0 || slot >= packetMenuRawItem.length()) {
			PluginLogger.warning(LOG_NODE, "访问 PacketMenu 缓存时发生错误: slot=" + slot + ", packetMenuSize=" + packetMenuRawItem.length());
			throw new IndexOutOfBoundsException();
		}
	}
	
	private void checkSizePacketMenu(Object[] array) {
		if (array.length != this.packetMenuRawItem.length()) {
			PluginLogger.warning(LOG_NODE, "访问 PacketMenu 缓存时发生错误: packetMenuSize=" + this.packetMenuRawItem.length() + ", parameterize=" + array.length);
			throw new IndexOutOfBoundsException();
		}
	}
	
	public AbstractPacketOutWindowOpen<?> createPacketWindowOpen() {
		val packetWindowOpen = packetAdapter.createPacketWindowOpen();
		packetWindowOpen.setWindowId(menu.getWindowId());
		packetWindowOpen.setMenuType(menu.getType());
		packetWindowOpen.setTitle(menu.getTitle());
		return packetWindowOpen;
	}
	
	public AbstractPacketOutWindowItems<?> createPacketWindowItemsForMenu() {
		val packetWindowItems = packetAdapter.createPacketWindowItems();
		packetWindowItems.setWindowId(menu.getWindowId());
		packetWindowItems.setRawItems(getCachePacketMenuRawItems());
		return packetWindowItems;
	}
	
	public AbstractPacketOutSetSlot<?> createPacketOutSetSlotForMenu(int slot) {
		val packetWindowSetSlot = packetAdapter.createPacketSetSlot();
		packetWindowSetSlot.setWindowId(menu.getWindowId());
		packetWindowSetSlot.setSlot(slot);
		packetWindowSetSlot.setRawItem(getCachePacketMenuRawItem(slot));
		return packetWindowSetSlot;
	}
	
	public AbstractPacketOutWindowItems<?> createPacketWindowItemsForInventory() {
		val packetWindowItems = packetAdapter.createPacketWindowItems();
		packetWindowItems.setWindowId(0);
		packetWindowItems.setRawItems(getCacheInventoryRawItems());
		return packetWindowItems;
	}
	
	public AbstractPacketOutSetSlot<?> createPacketOutSetSlotForInventory(int slot) {
		val packetWindowSetSlot = packetAdapter.createPacketSetSlot();
		packetWindowSetSlot.setWindowId(0);
		packetWindowSetSlot.setSlot(slot);
		packetWindowSetSlot.setRawItem(getCacheInventoryRawItem(slot));
		return packetWindowSetSlot;
	}
	
	public AbstractPacketOutSetSlot<?> createPacketOutSet(int rawSlot) {
		if (rawSlot < menu.getSize()) {
			return createPacketOutSetSlotForMenu(rawSlot);
		}
		return this.createPacketOutSetSlotForInventory(rawSlot - menu.getSize());
	}
	
	public void handlePacket(AbstractPacketOutSetSlot<?> packet) {
		if (packet.getWindowId() == 0) {
			this.updateCacheInventoryRawItem(packet.getSlot(), packet.getRawItem());
		}
	}
	
	public void handlePacket(AbstractPacketOutWindowItems<?> packet) {
		if (packet.getWindowId() == 0) {
			this.updateCacheInventoryRawItems(packet.getRawItems());
		}
	}

	public AbstractPacketOutCloseWindow<?> createPacketWindowCloseForMenu() {
		val packetWindowClose = packetAdapter.createPacketCloseWindow();
		packetWindowClose.setWindowId(menu.getWindowId());
		return packetWindowClose;
	}

}
