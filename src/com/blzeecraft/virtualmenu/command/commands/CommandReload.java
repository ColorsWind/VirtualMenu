package com.blzeecraft.virtualmenu.command.commands;

import org.bukkit.command.CommandSender;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.bound.BoundManager;
import com.blzeecraft.virtualmenu.command.BothSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.packet.PacketManager;
import com.blzeecraft.virtualmenu.settings.Settings;

@Args({})
@Name("reload")
@Help("/vm reload 重载菜单和配置文件")
public class CommandReload extends BothSubCommand {

	public CommandReload(VirtualMenuPlugin pl) {
		super(pl);
	}

	@Override
	public void onCommand(CommandSender sender, Object[] args) {
		PacketManager packetManager = pl.getPacketManager();
		BoundManager boundManager = pl.getBoundManager();
		boundManager.unregister();
		pl.getSettings().readSetting();
		packetManager.unregisterHandler();
		pl.getMenuManager().readMenu();
		packetManager.registerListener();
		boundManager.readBoundAction();
		boundManager.registerListener();
		Settings.sendMessage(sender, "成功重载菜单和配置文件");
	}


}
