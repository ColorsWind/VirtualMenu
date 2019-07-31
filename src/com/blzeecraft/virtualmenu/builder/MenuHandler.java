package com.blzeecraft.virtualmenu.builder;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.blzeecraft.virtualmenu.settings.Settings;


public class MenuHandler implements Listener {
	private final MenuBuilder builder;
	
	public MenuHandler(MenuBuilder builder) {
		super();
		this.builder = builder;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void handle(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		InventoryHolder holder = inv.getHolder();
		if (holder != null && holder instanceof MenuHolder) {
			MenuHolder mHolder = (MenuHolder) holder;
			String name = mHolder.getName();
			try {
				name = builder.saveInventory(inv, name);
				Settings.sendMessage((CommandSender) e.getPlayer(), "保存菜单到: ./plugins/VirtualMenuPlugin/builder/" + name);
			} catch (RuntimeException ex) {
				Settings.sendMessage((CommandSender) e.getPlayer(), "保存菜单失败(控制台差距完整报错):" + ex.getMessage());
				ex.printStackTrace();
			} 
		}
	} 

}
