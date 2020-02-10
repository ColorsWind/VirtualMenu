package com.blzeecraft.virtualmenu.core.action;

import java.util.EnumSet;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuEvent;
import com.blzeecraft.virtualmenu.core.config.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

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
