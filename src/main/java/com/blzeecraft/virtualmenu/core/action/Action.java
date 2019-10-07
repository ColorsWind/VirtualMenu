package com.blzeecraft.virtualmenu.core.action;

import java.util.EnumSet;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.singleline.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

public abstract class Action implements IAction, LoggerObject {

	protected final LogNode node;
	protected final Set<ClickType> types;

	
	public Action(LogNode node, ResolvedLineConfig rlc) {
		this.node = node;
		this.types = rlc.getAsOptEnumSet("click", ClickType.class).orElse(EnumSet.allOf(ClickType.class));
	}
	
	@Override
	public void accept(ClickEvent e) {
		if (types.contains(e.getType())) {
			try {
				execute(e.getUser());
			} catch (Exception ex) {
				ex.printStackTrace();
				PluginLogger.warning(node, "执行动作时发生异常, 该动作已经被忽略.");
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
