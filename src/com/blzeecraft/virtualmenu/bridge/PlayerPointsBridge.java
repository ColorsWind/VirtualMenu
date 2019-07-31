package com.blzeecraft.virtualmenu.bridge;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class PlayerPointsBridge {

	private static PlayerPoints playerPoints;

	public static boolean setupPlugin() {
		 if (! Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
			 return false;
	     }
		 playerPoints = JavaPlugin.getPlugin(PlayerPoints.class);
		 return true;
	}

	public static boolean hasValidPlugin() {
		return playerPoints != null;
	}


	public static boolean hasPoints(Player player, int minimum) {
		return playerPoints.getAPI().look(player.getUniqueId()) >= minimum;
	}

	public static boolean takePoints(Player player, int points) {
		return  playerPoints.getAPI().take(player.getUniqueId(), points);

	}




}