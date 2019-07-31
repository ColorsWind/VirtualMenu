package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.PlayerSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Arg;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.settings.Settings;

@Args({Arg.MENU})
@Name("open")
@Help("/vm open <menu> open <menu>")
public class CommandOpenPlayer extends PlayerSubCommand {

	public CommandOpenPlayer(VirtualMenuPlugin pl) {
		super(pl);
	}

	@Override
	public void onCommand(Player sender, Object[] args) {
		ChestMenu menu = (ChestMenu) args[0];
		pl.getPacketManager().openInventory(menu, sender);
		Settings.sendMessage(sender, "open menu " + menu.getName());
	}

}
