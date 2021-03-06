package com.blzeecraft.virtualmenu.core.conf.line;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.blzeecraft.virtualmenu.core.action.extension.ActionActionbar;
import com.blzeecraft.virtualmenu.core.action.extension.ActionBungeeCord;
import com.blzeecraft.virtualmenu.core.action.extension.ActionCloseMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpenMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionTell;
import com.blzeecraft.virtualmenu.core.action.extension.ActionTitle;
import com.blzeecraft.virtualmenu.core.condition.extension.ConditionEconomy;
import com.blzeecraft.virtualmenu.core.condition.extension.ConditionLevel;
import com.blzeecraft.virtualmenu.core.condition.extension.ConditionPermission;
import com.blzeecraft.virtualmenu.core.conf.InvalidConfigException;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

/**
 * 用于解析单行配置.
 * @author colors_wind
 *
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineConfigParser {
	// 常量池 单行配置解析器提示信息
	public static final String CORRECT_FORMAT_EXAMPLE = "正确示例tell{s=Testing}";
	public static final String ERROR_FORMAT_LEFT_BRACES = "格式错误(缺少\'{\'), " + CORRECT_FORMAT_EXAMPLE;
	public static final String ERROR_FORMAT_RIGHT_BRACES = "格式错误(缺少\'}\'), " + CORRECT_FORMAT_EXAMPLE;
	public static final String ERROR_FORMAT_EQUAL = "格式错误(缺少\'=\'), " + CORRECT_FORMAT_EXAMPLE;
	// 储存将解析的配置映射到对象(条件,动作etc)的方法
	private static final Map<String, BiFunction<LogNode, ResolvedLineConfig, LineConfigObject>> REGISTERED = new HashMap<>();

	static {
		// 加载预设
		registerLineCommand("actionbar", ActionActionbar::new);
		registerLineCommand("server", ActionBungeeCord::new);
		registerLineCommand("command", ActionCommand::new);
		registerLineCommand("console", ActionConsoleCommand::new);
		registerLineCommand("op", ActionOpCommand::new);
		registerLineCommand("open", ActionOpenMenu::new);
		registerLineCommand("tell", ActionTell::new);
		registerLineCommand("title", ActionTitle::new);
		registerLineCommand("close", ActionCloseMenu::new);
		registerLineCommand("money", ConditionEconomy::new);
		registerLineCommand("permission", ConditionPermission::new);
		registerLineCommand("level", ConditionLevel::new);
	}
	/**
	 * 用于解析被大括号括起来的字符串. 如{s=Testing,b=boolean}
	 * 特殊符号",=/"需要用"\"转义.
	 * @param s 待解析的字符串
	 * @return 解析结果
	 * @throws InvalidLineFormatException 如果无法解析该字符串
	 */
	public static ResolvedLineConfig parseEnclose(@NonNull String s) throws InvalidLineFormatException {
		if (s.isEmpty()) {
			return ResolvedLineConfig.EMPTY;
		}
		if (!s.startsWith("{")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_LEFT_BRACES);
		}
		if (!s.endsWith("}")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_RIGHT_BRACES);
		}
		return parseLine(s.substring(1, s.length() - 1));
	}


	/**
	 * 用于解析单行字符串如s=Testing,b=boolean.
	 * 特殊符号",=/"需要用"\"转义.
	 * @param s 待解析的字符串
	 * @return 解析结果
	 * @throws InvalidLineFormatException 如果无法解析该字符串
	 */
	public static ResolvedLineConfig parseLine(@NonNull String s) throws InvalidLineFormatException {
		char[] chars = s.toCharArray();
		boolean isKey = true; // 是否正在读取Key
		boolean isEscape = false; // 下一个字符是否转移
		StringBuilder input = new StringBuilder(); // 用于临时储存输入字符
		String key = null; // 用于临时储存key
		val values = new HashMap<String, String>();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (isEscape) { // 当前字符需要转义
				isEscape = false;
				input.append(c);
			} else if ('\\' == c) { // 下一个字符需要转义
				isEscape = true;
			} else if (!isKey && ',' == c) { // 每项的结尾
				isKey = true;
				values.put(key, input.toString());
				input = new StringBuilder();
			} else if (isKey && '=' == c) { // 值的开始
				isKey = false;
				key = input.toString();
				input = new StringBuilder();
			} else { // 正常的值
				input.append(c);
			}
		}
		// 尾部处理,检查是否处理完毕
		if (isKey) {
			throw new InvalidLineFormatException(ERROR_FORMAT_EQUAL);
		}
		// 尾部处理,将最后一对key-value放入map
		values.put(key, input.toString());
		return new ResolvedLineConfig(values);
	}

	public static boolean registerLineCommand(@NonNull String name,
			BiFunction<LogNode, ResolvedLineConfig, LineConfigObject> supplier) {
		return REGISTERED.putIfAbsent(name.toLowerCase(), supplier) == null;
	}

	public static boolean unregisterLineCommand(@NonNull String name) {
		return REGISTERED.remove(name.toLowerCase()) != null;
	}

	/**
	 * 用于解析完整字符串. 如console{command=say <player>}
	 * 特殊符号",=/"需要用"\"转义.
	 * 
	 * @param <T>   预期的对象类型
	 * @param node  日志节点
	 * @param s     待解析的字符串
	 * @param clazz 预期的对象类型的类
	 * @return 解析结果
	 * @throws InvalidLineFormatException 如果无法解析该字符串
	 * @throws InvalidLineObjectException 如果尝试读取解析的配置中无效的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends LineConfigObject> T parseFull(LogNode node, String s, Class<T> clazz)
			throws InvalidConfigException {
		int index = s.indexOf("{");
		if (index < 0) {
			if (s.endsWith("}")) {
				throw new InvalidLineFormatException(LineConfigParser.ERROR_FORMAT_LEFT_BRACES);
			}
			// s是简略写法
			index = s.length();
		}
		String prefix = s.substring(0, index);
		val supplier = REGISTERED.get(prefix.toLowerCase());
		if (supplier == null) {
			throw new InvalidLineCommandException("命令名为: " + prefix + " 的命令不存在, 请检查.");
		}
		ResolvedLineConfig lc;
		try {
			lc = LineConfigParser.parseEnclose(s.substring(index));
		} catch (InvalidLineFormatException e) {
			throw new InvalidLineFormatException(e.getMessage(), e);
		}
		val rlc = supplier.apply(node, lc);
		if (!clazz.isAssignableFrom(rlc.getClass())) {
			throw new ClassCastException("预期得到一个 " + clazz.getTypeName() + " 的实例却得到 " + rlc.getClass().getTypeName()
					+ " 的实例, 这很有可能是因为你将单行配置放在了错误的位置,如在条件标签下写了一个动作的单行配置.");
		}
		return (T) rlc;

	}

}
