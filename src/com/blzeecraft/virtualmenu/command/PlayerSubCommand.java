package com.blzeecraft.virtualmenu.command;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;

public abstract class PlayerSubCommand extends SubCommand {
	
	public PlayerSubCommand(VirtualMenuPlugin pl) {
		super(pl);
	}

	public abstract void onCommand(Player sender, Object[] args);

}
