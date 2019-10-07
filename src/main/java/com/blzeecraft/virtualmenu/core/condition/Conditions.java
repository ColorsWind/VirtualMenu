package com.blzeecraft.virtualmenu.core.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.config.singleline.InvalidLineFormatException;
import com.blzeecraft.virtualmenu.core.config.singleline.LineParser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import lombok.val;

/**
 * 条件常用方法
 * @author colors_wind
 *
 */
public class Conditions {

	/**
	 * 空的条件, 对任何 {@link ClickEvent} 都返回 {@link Optional#empty()}
	 */
	public static final ICondition EMPTY_CONDITION = e -> Optional.empty();

	/**
	 * 解析条件, 该方法一定会返回一个可用的结果
	 * 
	 * @param node 条件所属的日志节点
	 * @param line 待解析的字符串
	 * @return 解析结果
	 */
	public static ICondition parse(LogNode node, String line) {
		try {
			return LineParser.parseFull(node, line, Condition.class);
		} catch (InvalidLineFormatException e) {
			PluginLogger.severe(node, line);
		}
		PluginLogger.severe(node, "解析条件时发送严重错误, 已跳过该行.");
		return EMPTY_CONDITION;
	}

	/**
	 * 解析条件, 该方法一定会返回一个可用的结果
	 * 
	 * @param node 条件所属的日志节点
	 * @param line 待解析的字符串列表
	 * @return 解析结果
	 */
	public static ICondition parse(LogNode node, List<String> lines) {
		List<ICondition> cds = new ArrayList<>(lines.size());
		for (int i = 0; i < lines.size(); i++) {
			val subNode = node.sub("L" + i);
			val cd = parse(subNode, lines.get(i));
			if (cd != EMPTY_CONDITION) {
				cds.add(cd);
			} 
		}
		return cds.size() == 0 ? EMPTY_CONDITION : cds.size() == 1 ? cds.get(0) : new MultiCondition(cds);
	}

}
