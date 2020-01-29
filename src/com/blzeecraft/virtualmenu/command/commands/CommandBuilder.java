package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.PlayerSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;

@Args({})
@Name("builder")
@Help("/vm builder 创建新的菜单")
public class CommandBuilder extends PlayerSubCommand {

	public CommandBuilder(VirtualMenuPlugin pl) {
		super(pl);

	}


	@Override
	public void onCommand(Player sender, Object[] args) {
		Inventory inv = pl.getMenuBuilder().createInventory(sender);
		sender.closeInventory();
		sender.openInventory(inv);
	}

}