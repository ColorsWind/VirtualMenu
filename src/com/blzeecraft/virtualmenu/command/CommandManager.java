package com.blzeecraft.virtualmenu.command;

import java.util.HashSet;
import java.util.Set;

import com.blzeecraft.virtualmenu.InsensitiveMap;
import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.commands.CommandBuilder;
import com.blzeecraft.virtualmenu.command.commands.CommandInfo;
import com.blzeecraft.virtualmenu.command.commands.CommandOpenBoth;
import com.blzeecraft.virtualmenu.command.commands.CommandOpenPlayer;
import com.blzeecraft.virtualmenu.command.commands.CommandReload;

import lombok.Getter;

public class CommandManager {
	@Getter
	private static CommandManager instance;
	
	public static CommandManager init(VirtualMenuPlugin pl) {
		return instance = new CommandManager(pl);
	}

	private final VirtualMenuPlugin pl;
	protected final InsensitiveMap<BothSubCommand> bSubCommands;
	protected final InsensitiveMap<PlayerSubCommand> pSubCommands;
	protected final Set<SubCommand> subCommands;
	protected final CommandHandler executor;
	
	public CommandManager(VirtualMenuPlugin pl) {
		this.pl = pl;
		this.bSubCommands = new InsensitiveMap<>();
		this.pSubCommands = new InsensitiveMap<>();
		this.subCommands = new HashSet<>();
		this.executor = new CommandHandler(pl, this);
	}
	
	public void registerCommands() {
		registerCommand(new CommandReload(pl));
		registerCommand(new CommandInfo(pl));
		registerCommand(new CommandOpenBoth(pl));
		registerCommand(new CommandOpenPlayer(pl));
		registerCommand(new CommandBuilder(pl));
	}

	private boolean registerCommand(SubCommand command) {
		subCommands.add(command);
		if (command instanceof BothSubCommand) {
			for(String key : command.name) {
				bSubCommands.put(key, (BothSubCommand) command);
			}
			return true;
		} else if (command instanceof PlayerSubCommand) {
			for(String key : command.name) {
				pSubCommands.put(key, (PlayerSubCommand) command);
			}
			return true;
		}
		return false;
		
	}

	public void registerHandlers() {
		pl.getCommand("virtualmenu").setExecutor(executor);
	}


}
