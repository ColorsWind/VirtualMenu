package com.blzeecraft.virtualmenu.menu;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.menu.iiem.ExtendedIcon;
import com.blzeecraft.virtualmenu.menu.iiem.Icon;
import com.blzeecraft.virtualmenu.settings.Settings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class ViewPlayer {
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected final ChestMenu menu;
	protected final Player player;
	protected final Icon[] viewIcons;
	protected volatile long lastClick;
	
	public ViewPlayer(ChestMenu menu, Player player) {
		this.menu = menu;
		this.player = player;
		lastClick = -1;
		viewIcons = new Icon[menu.getSlots()];
		for(Entry<Integer, ExtendedIcon> en : menu.getIcons().entrySet()) {
			viewIcons[en.getKey()] = en.getValue().getIcon(player);
		}
	}
	
	
	
	public boolean canClick() {
		long current = System.currentTimeMillis();
		if(current - lastClick >= Settings.getInstance().getAntiSpam()) {
			lastClick = current;
			return true;
		}
		return false;
	}



	public ItemStack getItem(int slot) {
		Icon icon = viewIcons[slot];
		if (icon != null) {
			return icon.getItem(player);
		}
		return new ItemStack(Material.AIR);
	}



	public ItemStack[] getContents(Player p) {
		ItemStack[] items = new ItemStack[menu.getSlots()];
		for(int i=0;i<viewIcons.length;i++) {
			items[i] = getItem(i);
		}
		return items;
	}



	public Icon getIcon(int slot) {
		return viewIcons[slot];
	}

}
