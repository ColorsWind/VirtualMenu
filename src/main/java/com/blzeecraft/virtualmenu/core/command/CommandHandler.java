package com.blzeecraft.virtualmenu.core.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class CommandHandler {
	public static final LogNode LOG_NODE = LogNode.of("#CommandHandler");
	private static final Map<String, Collection<CommandMeta>> COMMAND = new HashMap<>();

	public boolean process(IUser<?> sender, String label, String[] args) {
		if (args.length == 0) {
			sendHelp(sender, label);
			return true;
		}
		Collection<CommandMeta> possibleMatch = COMMAND.get(args[0].toLowerCase());
		if (possibleMatch == null) {
			sender.sendMessageWithPrefix("§c找不到子命令: ", args[0]);
			for(Entry<String, Collection<CommandMeta>> entry : COMMAND.entrySet()) {
				if (calculateSimilarity(entry.getKey(), args[0]) > 0.75F) {
					suggestCommand(sender, label, entry.getValue());
				} else {
					sender.sendMessageWithPrefix("§a输入 /", label, " 可查看可用的命令.");
				}
			}
			return true;
		}
		for(CommandMeta meta : possibleMatch) {
			if (meta.getRequireArgsCount() == args.length -1) {
				return meta.invoke(sender, args);
			}
		}
		sender.sendMessageWithPrefix("§c参数个数不正确");
		suggestCommand(sender, label, possibleMatch);
		return true;
	}

	private void sendHelp(IUser<?> sender, String label) {
		sender.sendMessageWithPrefix("§a~~~~~~ §e§lVirtualMenu Help §a~~~~~");
		COMMAND.values().stream().flatMap(Collection::stream).distinct().filter(CommandMeta::visable).forEach(meta -> {
			sender.sendMessage("§a/" , label, " ", meta.getUsage());
		});	
		
		
	}

	private void suggestCommand(IUser<?> sender, String label, Collection<CommandMeta> command) {
		sender.sendMessageWithPrefix("§a是想输入这些命令吗? ");
		command.stream().filter(CommandMeta::visable).forEach(meta -> {
			sender.sendMessage("§b/" , label, " ", meta.getUsage());
		});	
		sender.sendMessageWithPrefix("§a输入 /", label, " 可查看帮助.");
	}

	/**
	 * 使用 Levenshtein 算法计算字符串相似度.
	 * 
	 * @param s1
	 * @param s2
	 * @return 相似度, 相似度越大表示两个字符串越相似.
	 */
	public static float calculateSimilarity(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return 1F;
		}
		if (s1 == null || s2 == null) {
			return 0F;
		}
		int distance = editDistance(s1, s2);
		return 1 - ((float) distance / Math.max(s1.length(), s2.length()));
	}

	private static int editDistance(String s1, String s2) {
		int s1_length = s1.length();
		int s2_length = s2.length();
		if (s1_length == 0 || s2_length == 0) {
			return 0;
		}
		int[][] array = new int[s1_length + 1][s2_length + 1];
		for (int i = 0; i <= s1_length; i++) {
			for (int j = 0; j <= s2_length; j++) {
				if (i == 0) {
					array[i][j] = j;
				} else if (j == 0) {
					array[i][j] = i;
				} else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					array[i][j] = array[i - 1][j - 1];
				} else {
					array[i][j] = 1 + Math.min(array[i - 1][j - 1], Math.min(array[i][j - 1], array[i - 1][j]));
				}
			}
		}
		return array[s1_length][s2_length];
	}

}
