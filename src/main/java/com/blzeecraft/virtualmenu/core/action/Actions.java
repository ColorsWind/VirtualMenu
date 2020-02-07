package com.blzeecraft.virtualmenu.core.action;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.config.line.InvalidLineFormatException;
import com.blzeecraft.virtualmenu.core.config.line.LineConfigParser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

import lombok.val;

/**
 * 动作常用方法
 * @author colors_wind
 *
 */
public class Actions {
	
	/**
	 * 空的动作, 对任何 {@link ClickEvent} 都不会执行任何操作
	 */
	public static IAction EMPTY_ACTION = e -> {};
	

	/**
	 * 解析动作, 该方法一定会返回一个可用的结果
	 * 
	 * @param node 条件所属的日志节点
	 * @param line 待解析的字符串
	 * @return 解析结果
	 */
	public static IAction parse(LogNode node, String line) {
		try {
			return LineConfigParser.parseFull(node, line, Action.class);
		} catch (InvalidLineFormatException e) {
			PluginLogger.severe(node, line);
		}
		PluginLogger.severe(node, "解析动作时发送严重错误, 已跳过该行.");
		return EMPTY_ACTION;
	}

	/**
	 * 解析动作, 该方法一定会返回一个可用的结果
	 * 
	 * @param node 条件所属的日志节点
	 * @param line 待解析的字符串列表
	 * @return 解析结果
	 */
	public static IAction parse(LogNode node, List<String> lines) {
		List<IAction> actions = new ArrayList<>(lines.size());
		for (int i = 0; i < lines.size(); i++) {
			val subNode = node.sub("L" + i);
			val action = parse(subNode, lines.get(i));
			if (action != EMPTY_ACTION) {
				actions.add(action);
			} 
		}
		return actions.size() == 0 ? EMPTY_ACTION : actions.size() == 1 ? actions.get(0) : new MultiAction(actions);
	}
	
	public static IAction wrap(IAction action, ICondition condition) {
		return event -> {if (condition.test(event)) action.accept(event);};
	}
	
	

}
