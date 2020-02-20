package com.blzeecraft.virtualmenu.bukkit.economy;

import java.util.OptionalDouble;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerPointsHook implements IEconomyHook {
	private PlayerPointsAPI api;

	@Override
	public boolean isPluginInstall() {
		return Bukkit.getPluginManager().isPluginEnabled("PlayerPoints");
	}

	@Override
	public boolean register() {
		PlayerPoints playerPoints = JavaPlugin.getPlugin(PlayerPoints.class);
		if (playerPoints == null) {
			return false;
		}
		api = playerPoints.getAPI();
		return true;
	}

	@Override
	public void unRegister() {
		api = null;
	}

	@Override
	public OptionalDouble getBanlance(Player player, String currency) {
		return OptionalDouble.of(api.look(player.getUniqueId()));
	}

	@Override
	/**
	 * 由于 PlayerPoints 只支持整数, 所有包含小数的请求都会拒绝.
	 */
	public boolean deposit(Player player, String currency, double amount) {
		if (!isInteger(amount)) {
			return false;
		}
		return api.give(player.getUniqueId(), (int) amount);
	}

	@Override
	/**
	 * 由于 PlayerPoints 只支持整数, 所有包含小数的请求都会拒绝.
	 */
	public boolean withdraw(Player player, String currency, double amount) {
		if (!isInteger(amount)) {
			return false;
		}
		return api.take(player.getUniqueId(), (int) amount);
	}

	public static final double ACCURACY = 1E-5;

	/**
	 * 判断double是否为整数
	 */
	public static boolean isInteger(double d) {
		return d - Math.floor(d) < ACCURACY;
	}

}
