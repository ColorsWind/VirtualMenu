package com.blzeecraft.virtualmenu.core.action.extension;


import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionCloseMenu extends Action {

	public ActionCloseMenu(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
	}

	@Override
	public void execute(IUser<?> user) {
		PacketManager.closeAllMenu();
	}



}
