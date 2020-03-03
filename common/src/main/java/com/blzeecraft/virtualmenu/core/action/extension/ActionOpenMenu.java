package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.conf.menu.MenuManager;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.val;

public class ActionOpenMenu extends Action {
	protected final String menu;
	
	public ActionOpenMenu(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.menu = rlc.getAsString("menu");
	}

	@Override
	public void execute(IUser<?> user) {
		val opt = MenuManager.getMenu(menu);
		if (opt.isPresent()) {
			user.openPacketMenu(opt.get());
		} else {
			user.sendMessage("找不到菜单: " + menu);
		}
	}

	@Override
	public String getKey() {
		return "open";
	}
	
	



}
