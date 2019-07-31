package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.ToString;

@ToString
public class ActionTell extends AbstractAction {

	public ActionTell(ILog parent, String raw) {
		super(parent, raw, ActionType.TELL);
	}

	@Override
	public void execute(Player p) {
		p.sendMessage(raw.replace("<player>", p.getName()));
	}

}
