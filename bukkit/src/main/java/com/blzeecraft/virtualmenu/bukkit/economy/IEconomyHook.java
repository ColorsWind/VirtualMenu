package com.blzeecraft.virtualmenu.bukkit.economy;

import java.util.OptionalDouble;

public interface IEconomyHook {
	
	OptionalDouble getBanlance(String currency);

	boolean deposit(String currency, double amount);

	boolean withdraw(String currency, double amount);

}
