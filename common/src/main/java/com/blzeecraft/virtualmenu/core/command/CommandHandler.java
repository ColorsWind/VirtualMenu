package com.blzeecraft.virtualmenu.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.command.extension.CommandList;
import com.blzeecraft.virtualmenu.core.command.extension.CommandOpenMenu;
import com.blzeecraft.virtualmenu.core.command.extension.CommandReload;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class CommandHandler {
	public static final LogNode LOG_NODE = LogNode.of("#CommandHandler");
	private static final Map<String, Collection<CommandMeta>> COMMAND = new HashMap<>();
	static {
		registerCommand(new CommandList());
		registerCommand(new CommandOpenMenu());
		registerCommand(new CommandReload());
	}
	/**
	 * 注册命令.
	 * @param command
	 * @return 成功添加的子命令个数, 命令名不同的子命令会单独计算.
	 */
	public static int registerCommand(SubCommandBase command) {
		List<String> firstArgument = Arrays.stream(command.name).map(String::toLowerCase).collect(Collectors.toList());
		long successTotal = CommandMeta.analysis(command).stream().mapToInt(meta -> {
			int success = (int) firstArgument.stream().mapToInt(name -> {
				Collection<CommandMeta> registered = COMMAND.get(name);
				if (registered == null) {
					COMMAND.put(name, new ArrayList<>(Arrays.asList(meta)));
					return 1;
				} else {
					if (registered.stream().anyMatch(
							registeredMeta -> meta.getRequireArgsCount() == registeredMeta.getRequireArgsCount())) {
						return 0;
					} else {
						registered.add(meta);
						return 1;
					}
				}
			}).count();
			return success;
		}).count();

		return (int) successTotal;
	}

	public static boolean process(IUser<?> sender, String label, String[] args) {
		if (args.length == 0) { // 无参数命令
			sendHelp(sender, label);
			return true;
		}
		Collection<CommandMeta> possibleMatch = COMMAND.get(args[0].toLowerCase());
		if (possibleMatch == null) { // 找不到子命令
			sender.sendMessageWithPrefix("§c找不到子命令: ", args[0]);
			Set<CommandMeta> toSuggest = COMMAND.entrySet().stream()
					.filter(entry -> calculateSimilarity(entry.getKey(), args[0]) > 0.75F).map(Entry::getValue)
					.flatMap(Collection::stream).collect(Collectors.toSet());
			if (toSuggest.size() == 0) {
				sender.sendMessageWithPrefix("§a输入 /", label, " 可查看可用的命令.");
			} else {
				suggestCommand(sender, label, toSuggest);
			}
			return true;
		}
		// 找到子命令
		Optional<CommandMeta> match = possibleMatch.stream()
				.filter(meta -> meta.getRequireArgsCount() == args.length - 1).findFirst();
		if (match.isPresent()) {
			// 参数个数正确
			return match.get().invoke(sender, args);
		} else {
			// 参数个数错误
			sender.sendMessageWithPrefix("§c参数个数不正确");
			suggestCommand(sender, label, possibleMatch);
			return true;
		}
	}

	private static void sendHelp(IUser<?> sender, String label) {
		sender.sendMessageWithPrefix("§a~~~~~~ §e§lVirtualMenu Help §a~~~~~");
		COMMAND.values().stream().flatMap(Collection::stream).distinct().filter(CommandMeta::visable)
				.filter(meta -> !(meta.isPlayerOnly() && sender.isConsole())).forEach(meta -> {
					sender.sendMessage("§a/", label, " ", meta.getUsage());
				});

	}

	private static void suggestCommand(IUser<?> sender, String label, Collection<CommandMeta> command) {
		sender.sendMessageWithPrefix("§a是想输入这些命令吗? ");
		command.stream().filter(CommandMeta::visable).filter(meta -> !(meta.isPlayerOnly() && sender.isConsole()))
				.forEach(meta -> {
					sender.sendMessage("§b/", label, " ", meta.getUsage());
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
