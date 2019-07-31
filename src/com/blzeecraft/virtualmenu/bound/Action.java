package com.blzeecraft.virtualmenu.bound;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public enum Action {
	
	RIGHT_SHIFT, LEFT_SHIFT, DROP_SHIFT, RIGHT_CLICK, LEFT_CLICK, DROP_ITEM;
	
	public static Action fromEvent(PlayerDropItemEvent e)  {
		if (e.getPlayer().isSneaking()) {
			return DROP_SHIFT;
		}
		return DROP_ITEM;
	}
	
	public static Action fromEvent(PlayerInteractEvent e)  {
		org.bukkit.event.block.Action action = e.getAction();
		if (e.getPlayer().isSneaking()) {
			switch(action) {
			case LEFT_CLICK_AIR:
				return LEFT_SHIFT;
			case LEFT_CLICK_BLOCK:
				return LEFT_SHIFT;
			case RIGHT_CLICK_AIR:
				return RIGHT_SHIFT;
			case RIGHT_CLICK_BLOCK:
				return RIGHT_SHIFT;
			default:
				break;
			}
		} else {
			switch(action) {
			case LEFT_CLICK_AIR:
				return LEFT_CLICK;
			case LEFT_CLICK_BLOCK:
				return LEFT_CLICK;
			case RIGHT_CLICK_AIR:
				return RIGHT_CLICK;
			case RIGHT_CLICK_BLOCK:
				return RIGHT_CLICK;
			default:
				break;
			}
		}
		return null;
	}

}
