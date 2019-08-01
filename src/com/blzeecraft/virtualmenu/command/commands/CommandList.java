package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.command.CommandSender;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.BothSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.settings.Settings;

@Args({})
@Name("list")
@Help("/vm list 查看菜单列表")
public class CommandList extends BothSubCommand {

	public CommandList(VirtualMenuPlugin pl) {
		super(pl);
	}

	@Override
	public void onCommand(CommandSender sender, Object[] args) {
		Settings.sendMessage(sender, "~~~~可用的菜单~~~");
		for(String menu : pl.getMenuManager().getMenus().keySet()) {
			Settings.sendMessage(sender, new StringBuilder(" -  ").append(menu).toString());
		}
	}


}
