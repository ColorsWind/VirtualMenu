package com.blzeecraft.virtualmenu.core.config.line;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.blzeecraft.virtualmenu.core.config.InvalidConfigException;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

/**
 * 用于解析单行配置
 * 
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

	/**
	 * 用于解析被大括号括起来的字符串如{s=Testing,b=boolean}
	 * 
	 * @param s 待解析的字符串
	 * @return 解析结果
	 * @throws InvalidLineFormatException 如果无法解析该字符串
	 */
	public static ResolvedLineConfig parseEnclose(@NonNull String s) throws InvalidLineFormatException {
		if (!s.startsWith("{")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_LEFT_BRACES);
		}
		if (!s.endsWith("}")) {
			throw new InvalidLineFormatException(ERROR_FORMAT_RIGHT_BRACES);
		}
		return parseLine(s.substring(1, s.length() - 1));
	}

	/**
	 * 用于解析单行字符串如s=Testing,b=boolean
	 * 
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

	// 储存将解析的配置映射到对象(条件,动作etc)的方法
	private static final Map<String, BiFunction<LogNode, ResolvedLineConfig, LineConfigObject>> REGISTERED = new HashMap<>();

	public static boolean registerAction(@NonNull String name,
			BiFunction<LogNode, ResolvedLineConfig, LineConfigObject> supplier) {
		return REGISTERED.putIfAbsent(name.toLowerCase(), supplier) == null;
	}

	public static boolean unregisterAction(@NonNull String name) {
		return REGISTERED.remove(name.toLowerCase()) != null;
	}

	/**
	 * 用于解析完整字符串
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
	public static <T> T parseFull(LogNode node, String s, Class<T> clazz)
			throws InvalidConfigException {
		int index = s.indexOf("{");
		if (index < 0) {
			throw new InvalidLineFormatException(LineConfigParser.ERROR_FORMAT_LEFT_BRACES);
		}
		String prefix = s.substring(0, index - 1);
		val supplier = REGISTERED.get(prefix.toLowerCase());
		if (supplier == null) {
			throw new NullPointerException("前缀为: " + prefix + " 的配置解析器未注册,请检查");
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
