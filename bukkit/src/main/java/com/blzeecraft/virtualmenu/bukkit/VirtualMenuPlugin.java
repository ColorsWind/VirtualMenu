package com.blzeecraft.virtualmenu.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import com.blzeecraft.virtualmenu.bukkit.economy.IEconomyHook;

import lombok.Getter;

public class VirtualMenuPlugin extends JavaPlugin {
	
	@Getter
	private static VirtualMenuPlugin instance;

	
	public IEconomyHook getEconomy() {
		return null;
	}

}
