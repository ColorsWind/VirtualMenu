package com.blzeecraft.virtualmenu.core.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
	
	public static CommandMeta analysis(Object instance, Method method) {
		boolean playerOnly = method.getAnnotation(PlayerOnly.class) != null;
		String usage = method.getAnnotation(Usage.class).value();
		Optional<String> requirePermission = Optional.ofNullable(method.getAnnotation(RequirePermission.class))
				.map(RequirePermission::value);
		// args
		Class<?>[] parameterTypes = method.getParameterTypes();
		@SuppressWarnings("unchecked")
		Function<String, Object>[] argsParsers = new Function[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Function<String, Object> parser = AVAILABLE_PARSERS.get(parameterTypes[i]);
			if (parser == null) {
				throw new IllegalArgumentException(
						"方法: " + method.toString() + " 不是一个合法的 CommandHandler. 调试信息: 不被支持的参数类型: " + parameterTypes[i]
								+ ". 仅支持: " + AVAILABLE_PARSERS.keySet());
			}
			argsParsers[i] = parser;
		}
		return new CommandMeta(instance, method, playerOnly, usage, requirePermission, parameterTypes, argsParsers);
	}

	private final Object instance;
	private final Method method;

	private final boolean playerOnly;
	private final String usage;
	private final Optional<String> requirePermission;
	private final Class<?>[] requireArgs;

	private final Function<String, Object>[] argsParsers;

	public int getRequireArgsCount() {
		return requireArgs.length;
	}


	public boolean invoke(IUser<?> sender, String[] args) {
		// args: arg1, arg2 ...
		// parameters: callstack, arg1, arg2, ...
		Object[] parameters = new Object[args.length + 1];
		for (int i = 0; i < args.length; i++) {
			try {
				parameters[i + 1] = this.argsParsers[i].apply(args[i]);
			} catch (IllegalCommandArgumentException e) {
				sender.sendMessageWithPrefix(e.format(i, args[i]));
				return false;
			}
		}
		Callstack callstack = new Callstack(sender, args);
		try {
			method.invoke(callstack, parameters);
		} catch (Exception e) {
			PluginLogger.warning(CommandHandler.LOG_NODE, "调用 " + method.toString() + "时出错. 调试信息: " + this.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
