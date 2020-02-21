package com.blzeecraft.virtualmenu.core.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.conf.menu.MenuManager;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 代表一个一个合法的 CommandHandler.
 * 
 * @author colors_wind
 * @date 2020-02-15
 */
@AllArgsConstructor
@Getter
@ToString
public class CommandMeta {
	public static final Map<Class<?>, Function<String, Object>> AVAILABLE_PARSERS = new HashMap<>();
	static {
		// String
		AVAILABLE_PARSERS.put(String.class, s -> s);

		// Integer
		AVAILABLE_PARSERS.put(Integer.class, s -> {
			try {
				return Integer.valueOf(s);
			} catch (NumberFormatException e) {
				throw new IllegalCommandArgumentException("§a无法将 %arg% 转换成一个整数.", e);
			}
		});

		// Long
		AVAILABLE_PARSERS.put(Long.class, s -> {
			try {
				return Long.valueOf(s);
			} catch (NumberFormatException e) {
				throw new IllegalCommandArgumentException("§a无法将 %arg% 转换成一个长整数.", e);
			}
		});

		// Double
		AVAILABLE_PARSERS.put(Long.class, s -> {
			try {
				return Double.valueOf(s);
			} catch (NumberFormatException e) {
				throw new IllegalCommandArgumentException("§a无法将 %arg% 转换成一个浮点数(小数).", e);
			}
		});

		// IUser
		AVAILABLE_PARSERS.put(IUser.class, s -> {
			return VirtualMenu.getUserExact(s).orElseThrow(() -> new IllegalCommandArgumentException("§a找不到玩家 %arg%."));
		});

		// IPacketMenu
		AVAILABLE_PARSERS.put(IPacketMenu.class, s -> {
			return MenuManager.getMenu(s).orElseThrow(() -> new IllegalCommandArgumentException("§a找不到菜单 %arg%."));
		});
	}

	public static List<CommandMeta> analysis(Object instance) {
		return Arrays.stream(instance.getClass().getMethods()).filter(method -> method.getAnnotation(Usage.class) != null)
				.map(method -> analysis(instance, method)).collect(Collectors.toList());
	}

	public static CommandMeta analysis(Object instance, Method method) {
		boolean playerOnly = method.getAnnotation(PlayerOnly.class) != null;
		String usage = method.getAnnotation(Usage.class).value();
		Optional<String> requirePermission = Optional.ofNullable(method.getAnnotation(RequirePermission.class))
				.map(RequirePermission::value);

		// e.g command argument: open      example     colors_wind
		//      argument parser: null    packetmenu  player
		//     method parameter: callstack m=example   u=colors_wind
		Class<?>[] methodParameter = method.getParameterTypes();
		@SuppressWarnings("unchecked")
		Function<String, Object>[] argumentParser = new Function[methodParameter.length];
		for (int i = 1; i < argumentParser.length; i++) {
			Function<String, Object> parser = AVAILABLE_PARSERS.get(methodParameter[i]);
			if (parser == null) {
				throw new IllegalArgumentException(
						"方法: " + method.toString() + " 不是一个合法的 CommandHandler. 调试信息: 不被支持的参数类型: " + methodParameter[i]
								+ ". 仅支持: " + AVAILABLE_PARSERS.keySet());
			}
			argumentParser[i] = parser;
		}
		return new CommandMeta(instance, method, playerOnly, usage, requirePermission, methodParameter, argumentParser);
	}

	private final Object instance;
	private final Method method;

	private final boolean playerOnly;
	private final String usage;
	private final Optional<String> requirePermission;
	private final Class<?>[] methodParameterType;

	private final Function<String, Object>[] argumentParser;

	public int getRequireArgsCount() {
		return methodParameterType.length - 1;
	}

	public boolean visable() {
		return !usage.isEmpty();
	}

	/**
	 * 执行命令.
	 * @param sender
	 * @param commandArgument
	 * @return
	 */
	public boolean invoke(IUser<?> sender, String[] commandArgument) {
		if (sender.isConsole() && this.playerOnly) {
			sender.sendMessageWithPrefix("§c该命令只能由玩家执行.");
			return true;
		}
		if (this.requirePermission.isPresent() && !sender.hasPermission(this.requirePermission.get())) {
			sender.sendMessageWithPrefix("§c你没有权限执行该命令.");
			return true;
		}
		// e.g command argument: open        example     colors_wind
		//      argument parser: null        packetmenu  player
		//       convent object: null        m=example   u=colors_wind
		//     method parameter: callstack   m=example   u=colors_wind
		Object[] conventObject = new Object[commandArgument.length];
		for (int i = 1; i < commandArgument.length; i++) {
			try {
				conventObject[i] = this.argumentParser[i].apply(commandArgument[i]);
			} catch (IllegalCommandArgumentException e) {
				sender.sendMessageWithPrefix(e.format(i, commandArgument[i]));
				return false;
			}
		}
		Object[] methodParameter = new Object[this.methodParameterType.length];
		methodParameter[0] = new Callstack(sender, commandArgument);
		System.arraycopy(conventObject, 1, methodParameter, 1, methodParameter.length - 1);
		try {
			method.invoke(instance, methodParameter);
		} catch (Exception e) {
			PluginLogger.warning(CommandHandler.LOG_NODE, "调用 " + method.toString() + "时出错. 调试信息: " + this.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
