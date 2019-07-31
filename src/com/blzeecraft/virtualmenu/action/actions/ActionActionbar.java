package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.title.TitleAPI;

public class ActionActionbar extends AbstractAction {

	public ActionActionbar(ILog parent, String raw) {
		super(parent, raw, ActionType.ACTIONBAR);
	}

	@Override
	public void execute(Player p) {
		TitleAPI.sendActionBar(p, raw.replace("<player>", p.getName()));
	}

}
