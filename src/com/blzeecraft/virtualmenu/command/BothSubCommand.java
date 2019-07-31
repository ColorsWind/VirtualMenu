package com.blzeecraft.virtualmenu.command;

import org.bukkit.command.CommandSender;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;

import lombok.Getter;

@Getter
public abstract class BothSubCommand extends SubCommand {

	public BothSubCommand(VirtualMenuPlugin pl) {
		super(pl);
	}

	public abstract void onCommand(CommandSender sender, Object[] args);


}
