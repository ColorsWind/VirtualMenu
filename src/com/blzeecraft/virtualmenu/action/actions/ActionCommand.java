package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

import me.clip.placeholderapi.PlaceholderAPI;

public class ActionCommand extends AbstractAction {

	public ActionCommand(ILog parent, String raw) {
		super(parent, raw, ActionType.COMMAND);
	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		String text = raw;
		if (isPlaceholderAPI) {
			text = PlaceholderAPI.setPlaceholders(p, text);
		}
		this.execute(p, text.replace("<player>", p.getName()));
	}

}
