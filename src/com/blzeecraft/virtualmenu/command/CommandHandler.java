package com.blzeecraft.virtualmenu.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.SubCommand.ConvertArgs;
import com.blzeecraft.virtualmenu.settings.Settings;

public class CommandHandler implements CommandExecutor {
	
	protected final CommandManager manager;
	protected final VirtualMenuPlugin pl;
	
	public CommandHandler(VirtualMenuPlugin pl ,CommandManager manager) {
		this.pl = pl;
		this.manager = manager;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String labal, String[] args) {
		int length = args.length;
		if (length >= 1) {
			String key = args[0];
			List<SubCommand> cmds = manager.subCommands.get(key);
			if(cmds == null) {
				// 找不到命令
				sendNoSubCommand(sender);
			} else {
				// 找到命令，开始筛选合适的命令
				for(SubCommand cmd : cmds) {
					int clength = length - 1;
					if (cmd.requireArgs.length == clength) {
						// 参数个数合适，尝试转换参数
						String[] cargs = new String[clength];
						System.arraycopy(args, 1, cargs, 0, clength);
						ConvertArgs ca = cmd.new ConvertArgs(cargs);
						if (ca.getErrorIndex() == -1) {
							//成功转换
							if(cmd instanceof PlayerSubCommand) {
								// 玩家命令
								if (sender instanceof Player) {
									// 命令发出者是玩家
									((PlayerSubCommand) cmd).onCommand((Player) sender, ca.getConvertArgs());
								} else {
									// 命令发出者不是玩家(有可能是控制台发出)
									senderPlayerOnly(sender, cmds);
								}
							} else if (cmd instanceof BothSubCommand) {
								// 通用命令
								((BothSubCommand) cmd).onCommand(sender, ca.getConvertArgs());
							}
						} else {
							//转换失败
							sendErrorMessage(sender, ca.getErrorMessage(), cmds);
						}

						return true;
					} 
				}
				sendLengthError(sender, cmds);
			}
		} else {
			sendHelp(sender);
		}
		return true;
	}

	private void senderPlayerOnly(CommandSender sender, List<SubCommand> advice) {
		Settings.sendMessage(sender, "该命令只能由玩家发出");
		sendHelp(sender, advice);
	}

	private void sendErrorMessage(CommandSender sender, String message, List<SubCommand> advice) {
		Settings.sendMessage(sender, message);
		sendHelp(sender, advice);
		
	}
	private void sendLengthError(CommandSender sender, List<SubCommand> advice) {
		Settings.sendMessage(sender, "参数数量错误");
		sendHelp(sender, advice);
		
	}

	private void sendNoSubCommand(CommandSender sender) {
		Settings.sendMessage(sender, "找不到命令");
		sendHelp(sender);
	}
	public void sendHelp(CommandSender sender) {
		Settings.sendMessage(sender, "~~~~VirtualMenu命令列表~~~");
		for(SubCommand sc : manager.registered) {
			Settings.sendMessage(sender, sc.help);
		} 
	}
	
	public void sendHelp(CommandSender sender, List<SubCommand> advice) {
		Settings.sendMessage(sender, "~~~~VirtualMenu相关命令~~~");
		for(SubCommand sc : advice) {
			Settings.sendMessage(sender, sc.help);
		} 
	}

}
