package com.blzeecraft.virtualmenu.core.condition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.blzeecraft.virtualmenu.core.conf.InvalidConfigException;
import com.blzeecraft.virtualmenu.core.conf.line.LineConfigParser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import lombok.val;

/**
 * 条件的工具类
 * @author colors_wind
 *
 */
public class ConditionUtils {

	/**
	 * 空的条件, 对任何 {@link IconActionEvent} 都返回 {@link Optional#empty()}
	 */
	public static final ICondition EMPTY_CONDITION = e -> Optional.empty();

	/**
	 * 解析条件, 该方法一定会返回一个可用的结果
	 * 
	 * @param node 条件所属的 LogNode
	 * @param line 待解析的字符串
	 * @return 解析结果
	 */
	public static ICondition parse(LogNode node, String line) {
		try {
			return LineConfigParser.parseFull(node, line, Condition.class);
		} catch (InvalidConfigException e) {
			PluginLogger.warning(node, line);
		}
		PluginLogger.severe(node, "解析条件时发送严重错误, 已跳过该行.");
		return EMPTY_CONDITION;
	}

	/**
	 * 解析条件, 该方法一定会返回一个可用的结果.
	 * 
	 * @param node 条件所属的日志节点
	 * @param line 待解析的字符串列表
	 * @return 解析结果, 如果没有任何结果, 返回 {@link #EMPTY_CONDITION}
	 */
	public static ICondition  parse(LogNode node, List<String> lines) {
		List<Condition> condtions = IntStream.range(0, lines.size()).mapToObj(i -> {
			val subNode = node.sub("L" + (i + 1));
			val condition = parse(subNode, lines.get(i));
			return condition;
		}).filter(action -> action != EMPTY_CONDITION).map(Condition.class::cast).collect(Collectors.toList());
		return condtions.size() == 0 ? EMPTY_CONDITION : condtions.size() == 1 ? condtions.get(0) : new MultiCondition(condtions);
	}

}
