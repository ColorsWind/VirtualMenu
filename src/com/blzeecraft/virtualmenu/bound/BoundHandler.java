package com.blzeecraft.virtualmenu.bound;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.menu.ChestMenu;

public class BoundHandler implements Listener {
	protected final BoundManager manager;
	
	public BoundHandler(BoundManager manager) {
		this.manager = manager;
	}
	
	@EventHandler
	private void handle(PlayerDropItemEvent e)  {
		Action action = Action.fromEvent(e);
		if (action != null) {
			ChestMenu menu = manager.getByAction(e.getItemDrop().getItemStack().getType(), action);
			if (menu != null) {
				manager.pl.getPacketManager().openInventory(menu, e.getPlayer());
			}
		}
	}  
	
	@EventHandler
	private void handle(PlayerInteractEvent e)  {
		Action action = Action.fromEvent(e);
		if (action != null) {
			ItemStack item = e.getItem();
			if (item != null) {
				ChestMenu menu = manager.getByAction(item.getType(), action);
				if (menu != null) {
					manager.pl.getPacketManager().openInventory(menu, e.getPlayer());
				}
			}

		}
	}
	
	@EventHandler
	public void handle(PlayerCommandPreprocessEvent e) {
		String m = e.getMessage();
		ChestMenu menu = manager.getByCommand(m.substring(1, m.length()));
		if (menu != null) {
			e.setCancelled(true);
			manager.pl.getPacketManager().openInventory(menu, e.getPlayer());
		}
	}
	

}
