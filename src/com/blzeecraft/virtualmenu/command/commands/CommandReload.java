package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.command.CommandSender;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.BothSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.packet.PacketManager;
import com.blzeecraft.virtualmenu.settings.Settings;

@Args({})
@Name("reload")
@Help("/vm reload reload config and menus")
public class CommandReload extends BothSubCommand {

	public CommandReload(VirtualMenuPlugin pl) {
		super(pl);
	}

	@Override
	public void onCommand(CommandSender sender, Object[] args) {
		PacketManager packetManager = pl.getPacketManager();
		pl.getSettings().readSetting();
		packetManager.unregisterHandler();
		pl.getMenuManager().readMenu();
		packetManager.registerListener();
		Settings.sendMessage(sender, "Successfully reloaded config and menus");
	}


}
