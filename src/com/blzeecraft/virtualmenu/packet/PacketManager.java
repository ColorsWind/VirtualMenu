package com.blzeecraft.virtualmenu.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.menu.EventType;
import com.blzeecraft.virtualmenu.menu.ViewPlayer;
import com.blzeecraft.virtualmenu.packet.packets.PacketCloseWindow;
import com.blzeecraft.virtualmenu.packet.packets.PacketOpenWindow;
import com.blzeecraft.virtualmenu.packet.packets.PacketSetSlot;
import com.blzeecraft.virtualmenu.packet.packets.PacketWindowItems;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.concurrency.ConcurrentPlayerMap;

import lombok.Getter;
import lombok.NonNull;

@NonNull
public class PacketManager {
	@Getter
	private static PacketManager instance;
	
	public static PacketManager init(VirtualMenuPlugin pl) {
		return instance = new PacketManager(pl);
	}
	
	protected final VirtualMenuPlugin pl;
	protected final ConcurrentMap<Player, ChestMenu> openMenus;
	// 保存了目前菜单对玩家显示的物品,更新变量时应该更新这里
	protected final ConcurrentMap<Player, ItemStack[]> cacheItems;
	protected PacketCloseWindowHandler closeHandler;
	protected PacketWindowClickHandler clickHandler;
	
	protected PacketManager(VirtualMenuPlugin pl) {
		this.pl = pl;
		openMenus  = ConcurrentPlayerMap.usingName();
		cacheItems = ConcurrentPlayerMap.usingName();
		closeHandler = new PacketCloseWindowHandler(pl, this);
		clickHandler = new PacketWindowClickHandler(pl ,this);
	}
	
	public boolean registerListener() {
		ProtocolLibrary.getProtocolManager().addPacketListener(closeHandler);
		ProtocolLibrary.getProtocolManager().addPacketListener(clickHandler);
		Bukkit.getPluginManager().registerEvents(closeHandler, pl);
		return true;
	}
	
	public boolean openInventory(ChestMenu menu, Player p) {
		ChestMenu origin = openMenus.put(p, menu);
		if (origin != null) {
			closeInventory(p, origin);
		}
		PacketOpenWindow open = new PacketOpenWindow(menu.getID(), menu.getTitle(), menu.getType());
		ViewPlayer v = menu.addViewer(p);
		ItemStack[] item = v.getContents(p);
		PacketWindowItems items = new PacketWindowItems(menu.getID(), item);
		cacheItems.put(p, item);
		try {
			open.send(p);
			items.send(p);
			menu.click(EventType.OPEN, -999, p, null, null);
			return true;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 强制关闭玩家打开的菜单
	 * @param p
	 * @return
	 */
	public boolean closeInventory(Player p) {
		ChestMenu menu = openMenus.get(p);
		if (menu != null) {
			PacketCloseWindow close = new PacketCloseWindow(menu.getID());
			try {
				close.send(p);
				cleanData(p);
				return true;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 强制关闭玩家打开的菜单
	 * @param p
	 * @param menu
	 * @return
	 */
	public boolean closeInventory(Player p, ChestMenu menu) {
		if (menu != null) {
			PacketCloseWindow close = new PacketCloseWindow(menu.getID());
			try {
				close.send(p);
				cleanData(p);
				return true;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	
	public void unregisterHandler() {
		ProtocolLibrary.getProtocolManager().removePacketListeners(pl);
		HandlerList.unregisterAll(closeHandler);
		cacheItems.clear();
		Iterator<Entry<Player, ChestMenu>> it = openMenus.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Player, ChestMenu> en = it.next();
			PacketCloseWindow close = new PacketCloseWindow(en.getValue().getID());
			try {
				close.send(en.getKey());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			en.getValue().click(EventType.CLOSE, -999, en.getKey(), null, null);
			it.remove();
		}
	}
	public void cleanData(Player p, int id) {
		ChestMenu menu = openMenus.get(p);
		if (menu != null && menu.getID() == id) {
			cleanData(p);
		}
	}

	/**
	 * 除非插件卸载，否则玩家关闭菜单都会调用这个方法
	 * @param p
	 * @see #unregisterHandler()
	 */
	public void cleanData(Player p) {
		ChestMenu menu = openMenus.remove(p);
		if (menu != null) {
			menu.click(EventType.CLOSE, -999, p, null, null);
			if (menu != null) {
				menu.removeViewer(p);
			}
			cacheItems.remove(p);
		}
	}

	public void updateItem(ViewPlayer v, int slot, ItemStack replace) {
		ItemStack[] origin = cacheItems.get(v.getPlayer());
		ItemStack item = origin[slot];
		ItemMeta meta1 = item.getItemMeta();
		ItemMeta meta2 = replace.getItemMeta();
		if ((meta1 == null && meta2 != null) || (meta1 != null && meta2 == null)) {
			return;
		}
		if (meta1.hasDisplayName()) {
			if (!meta1.getDisplayName().equals(meta2.getDisplayName())) {
				return;
			}
		} else if (meta2.hasDisplayName()){
			return;
		}
		if (meta1.hasDisplayName()) {
			if (!meta1.getLore().equals(meta2.getLore())) {
				return;
			}
		} else if (meta2.hasLore()){
			return;
		}
		origin[slot] = replace;
		PacketSetSlot packet = new PacketSetSlot(v.getMenu().getID(), slot, item);
		try {
			packet.send(v.getPlayer());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}


}
