package com.blzeecraft.virtualmenu.core.action;

import java.util.EnumSet;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.conf.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickType;
import com.blzeecraft.virtualmenu.core.user.IUser;

/**
 * 这个类代表一个动作.这个类是不可变的.<p>
 * 动作可以是一个命令,也可以是诸如发送actionbar这样的其他操作.<p>
 * 动作是由 {@link MenuActionEvent} 或 {@link IconActionEvent} 触发的,<p>
 * 两者分别对应菜单配置中events和icon下的action.
 * @author colors_wind
 * @date 2020-02-10
 */
public abstract class Action implements IAction, LoggerObject {

	protected final LogNode node;
	protected final Set<ClickType> types;

	
	public Action(LogNode node, ResolvedLineConfig rlc) {
		this.node = node;
		this.types = rlc.getAsOptEnumSet("click", ClickType.class).orElse(EnumSet.allOf(ClickType.class));
	}
	
	@Override
	public void accept(MenuEvent event) {
		if (types.contains(event.getClickType())) {
			try {
				execute(event.getUser());
			} catch (Exception e) {
				PluginLogger.warning(node, "处理菜单事件时发生严重异常.");
				e.printStackTrace();
			}
		}
	}

	public Action(LogNode node) {
		this.node = node;
		this.types = EnumSet.allOf(ClickType.class);
	}
	
	@Override
	public LogNode getLogNode() {
		return node;
	}
	

	public abstract void execute(IUser<?> user);

}
