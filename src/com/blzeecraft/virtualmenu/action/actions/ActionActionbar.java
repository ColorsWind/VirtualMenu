package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.title.TitleAPI;

import me.clip.placeholderapi.PlaceholderAPI;

public class ActionActionbar extends AbstractAction {

	public ActionActionbar(ILog parent, String raw) {
		super(parent, raw, ActionType.ACTIONBAR);
	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		String text = raw;
		if (isPlaceholderAPI) {
			text = PlaceholderAPI.setPlaceholders(p, text);
		}
		TitleAPI.sendActionBar(p, text.replace("<player>", p.getName()));
	}

}
