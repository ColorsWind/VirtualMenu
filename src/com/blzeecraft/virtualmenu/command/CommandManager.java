package com.blzeecraft.virtualmenu.command;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.InsensitiveMap;
import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.commands.CommandBuilder;
import com.blzeecraft.virtualmenu.command.commands.CommandEditor;
import com.blzeecraft.virtualmenu.command.commands.CommandInfo;
import com.blzeecraft.virtualmenu.command.commands.CommandList;
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
	protected final InsensitiveMap<List<SubCommand>> subCommands;
	protected final List<SubCommand> registered;
	protected final CommandHandler executor;
	
	public CommandManager(VirtualMenuPlugin pl) {
		this.pl = pl;
		this.subCommands = new InsensitiveMap<>();
		this.registered = new ArrayList<>();
		this.executor = new CommandHandler(pl, this);
	}
	
	public void registerCommands() {
		registerCommand(new CommandReload(pl));
		registerCommand(new CommandInfo(pl));
		registerCommand(new CommandOpenBoth(pl));
		registerCommand(new CommandOpenPlayer(pl));
		registerCommand(new CommandBuilder(pl));
		registerCommand(new CommandEditor(pl));
		registerCommand(new CommandList(pl));
	}

	private boolean registerCommand(SubCommand command) {
		List<SubCommand> cmds = new ArrayList<>();
		for(String key : command.name) {
			List<SubCommand> origin = subCommands.putIfAbsent(key, cmds);
			if (origin != null) {
				origin.add(command);
			} else {
				cmds.add(command);
			}
		}
		return registered.add(command);
	}

	public void registerHandlers() {
		pl.getCommand("virtualmenu").setExecutor(executor);
	}


}
