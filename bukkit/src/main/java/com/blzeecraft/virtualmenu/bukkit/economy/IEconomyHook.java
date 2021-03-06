package com.blzeecraft.virtualmenu.bukkit.economy;

import java.util.OptionalDouble;

import org.bukkit.entity.Player;

public interface IEconomyHook {
	
	boolean isPluginInstall();
	
	boolean register();
	
	void unRegister();
	
	OptionalDouble getBanlance(Player player, String currency);

	boolean deposit(Player player, String currency, double amount);

	boolean withdraw(Player player, String currency, double amount);

}
