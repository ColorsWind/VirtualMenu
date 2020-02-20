package com.blzeecraft.virtualmenu.bukkit.economy;

import java.util.OptionalDouble;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.economy.Account;
import me.xanium.gemseconomy.economy.AccountManager;
import me.xanium.gemseconomy.economy.Currency;

public class GemsEconomyHook implements IEconomyHook {

	@Override
	public boolean isPluginInstall() {
		return Bukkit.getPluginManager().isPluginEnabled("GemsEconomy");
	}

	@Override
	public boolean register() {
		if (JavaPlugin.getPlugin(GemsEconomy.class) == null) {
			return false;
		}
		try {
			Class.forName("me.xanium.gemseconomy.economy.AccountManager");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;

	}

	@Override
	public void unRegister() {
	}

	@Override
	public OptionalDouble getBanlance(Player player, String currency) {
		Currency c = AccountManager.getCurrency(currency);
		if (c == null) {
			return OptionalDouble.empty();
		}
		Account acc = AccountManager.getAccount(player);
		if (acc == null) {
			return OptionalDouble.empty();
		}
		return OptionalDouble.of(acc.getBalance(c));
	}

	@Override
	public boolean deposit(Player player, String currency, double amount) {
		Currency c = AccountManager.getCurrency(currency);
		if (c == null) {
			return false;
		}
		Account acc = AccountManager.getAccount(player);
		if (acc == null) {
			return false;
		}
		return acc.deposit(c, amount);
	}

	@Override
	public boolean withdraw(Player player, String currency, double amount) {
		Currency c = AccountManager.getCurrency(currency);
		if (c == null) {
			return false;
		}
		Account acc = AccountManager.getAccount(player);
		if (acc == null) {
			return false;
		}
		return acc.withdraw(c, amount);
	}

}
