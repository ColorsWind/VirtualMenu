package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.PlayerSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Arg;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.menu.ChestMenu;

@Args({Arg.MENU})
@Name("edit")
@Help("/vm edit <menu> 编辑已经存在的菜单")
public class CommandEditor extends PlayerSubCommand {

	public CommandEditor(VirtualMenuPlugin pl) {
		super(pl);
	}


	@Override
	public void onCommand(Player sender, Object[] args) {
		ChestMenu menu = (ChestMenu) args[0];
		Inventory inv = pl.getMenuBuilder().createInventory(sender, menu);
		sender.closeInventory();
		sender.openInventory(inv);
	}

}
