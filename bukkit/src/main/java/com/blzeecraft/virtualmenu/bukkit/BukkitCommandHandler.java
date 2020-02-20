package com.blzeecraft.virtualmenu.bukkit;

import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.core.command.CommandHandler;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class BukkitCommandHandler implements CommandExecutor {
	private final BukkitPlatform platform;

	public BukkitCommandHandler(VirtualMenuPlugin plugin) {
		this.platform = plugin.getPlatformAdapter();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Optional<IUser<?>> optUser = platform.getUserExact((Player)sender);
			if (optUser.isPresent()) {
				return CommandHandler.process(optUser.get(), label, args);
			}
		} 
		return CommandHandler.process(platform.getConsole(), label, args);
	}

}
