package com.blzeecraft.virtualmenu.bridge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyBridge {
	private static Economy econ;

	public static boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}

	public static boolean hasValidEconomy() {
		return econ != null;
	}

	public static boolean hasMoney(Player player, double money) {
		if (hasValidEconomy()) {
			return econ.has(player, money);
		}
		return false;
	}

	public static boolean takeMoney(Player player, double amount) {
		if(hasValidEconomy()) {
			EconomyResponse response = econ.withdrawPlayer(player, amount);
			return response.transactionSuccess();
		}
		return false;
	}
}