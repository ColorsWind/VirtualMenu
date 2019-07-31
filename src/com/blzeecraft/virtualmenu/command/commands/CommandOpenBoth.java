package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.BothSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Arg;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.settings.Settings;

@Args({Arg.MENU, Arg.PLAYER})
@Name("open")
@Help("/vm open <menu> <player> open <menu> for <player>")
public class CommandOpenBoth extends BothSubCommand {

	public CommandOpenBoth(VirtualMenuPlugin pl) {
		super(pl);

	}

	@Override
	public void onCommand(CommandSender sender, Object[] args) {
		ChestMenu menu = (ChestMenu) args[0];
		Player p = (Player) args[1];
		pl.getPacketManager().openInventory(menu, p);
		Settings.sendMessage(sender, "为 " + p.getName() + " 打开菜单 " + menu.getName());
	}

}
