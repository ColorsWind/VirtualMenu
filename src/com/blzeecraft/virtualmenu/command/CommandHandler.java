package com.blzeecraft.virtualmenu.command;

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
			int clength = length - 1;
			String[] cargs = new String[clength];
			System.arraycopy(args, 1, cargs, 0, clength);
			PlayerSubCommand psc = manager.pSubCommands.get(key);
			//优先从psc中查找命令
			if (psc == null) {
				//psc中找不到命令，尝试从bsc中查找
				BothSubCommand bsc = manager.bSubCommands.get(key);
				if (bsc == null) {
					//psc，bsc都找不到命令
					sendNoSubCommand(sender);
				} else {
					//bsc中找到命令，检查参数个数
					if (checkLength(bsc, clength)) {
						//参数个数正确，尝试转换参数
						ConvertArgs ca = bsc.new ConvertArgs(cargs);
						if (ca.getErrorIndex() == -1) {
							//成功转换
							bsc.onCommand(sender, ca.getConvertArgs());
						} else {
							//转换失败
							sendErrorMessage(sender, ca.getErrorMessage());
						}
					} else {
						//参数长度错误
						sendLengthError(sender);
					}
				}
			} else {
				//psc找到命令，检查是否为玩家
				if (sender instanceof Player) {
					//是玩家,下一步检查参数个数
					Player p = (Player) sender;
					if (checkLength(psc, clength)) {
						//参数个数正确，尝试转换参数
						ConvertArgs ca = psc.new ConvertArgs(cargs);
						if (ca.getErrorIndex() == -1) {
							//成功转换
							psc.onCommand(p, ca.getConvertArgs());
						} else {
							//转换失败
							sendErrorMessage(sender, ca.getErrorMessage());
						}
					} else {
						//参数长度错误
						sendLengthError(sender);
					}
				}
			}
		} else {
			sendHelp(sender);
		}
		return true;
	}

	private void sendErrorMessage(CommandSender sender, String message) {
		Settings.sendMessage(sender, message);
		sendHelp(sender);
		
	}
	private void sendLengthError(CommandSender sender) {
		Settings.sendMessage(sender, "参数数量错误");
		sendHelp(sender);
		
	}
	private boolean checkLength(SubCommand bsc, int length) {
		return bsc.requireArgs.length == length;
	}
	private void sendNoSubCommand(CommandSender sender) {
		Settings.sendMessage(sender, "找不到命令");
		sendHelp(sender);
	}
	public void sendHelp(CommandSender sender) {
		Settings.sendMessage(sender, "~~~~VirtualMenu命令列表~~~");
		for(SubCommand sc : manager.subCommands) {
			Settings.sendMessage(sender, sc.help);
		}
		
	}

}
