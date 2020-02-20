package com.blzeecraft.virtualmenu.bukkit.economy;

import java.util.OptionalDouble;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultHook implements IEconomyHook {
	private Economy econ = null;

	@Override
	public boolean isPluginInstall() {
		return Bukkit.getPluginManager().isPluginEnabled("Vault");
	}

	@Override
	public boolean register() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	@Override
	public void unRegister() {
		econ = null;
	}

	@Override
	public OptionalDouble getBanlance(Player player, String currency) {
		return OptionalDouble.of(econ.getBalance(player));
	}

	@Override
	public boolean deposit(Player player, String currency, double amount) {
		EconomyResponse respone = econ.depositPlayer(player, amount);
		return respone.transactionSuccess();
	}

	@Override
	public boolean withdraw(Player player, String currency, double amount) {
		EconomyResponse respone = econ.withdrawPlayer(player, amount);
		return respone.transactionSuccess();
	}

}
