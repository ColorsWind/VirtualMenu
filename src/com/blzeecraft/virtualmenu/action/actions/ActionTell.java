package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;

@ToString
public class ActionTell extends AbstractAction {
	protected final String message;

	public ActionTell(ILog parent, String raw) {
		super(parent, raw, ActionType.TELL);
		this.message = ChatColor.translateAlternateColorCodes('&', raw);
	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		String text = message;
		if (isPlaceholderAPI) {
			text = PlaceholderAPI.setPlaceholders(p, text);
		}
		p.sendMessage(text.replace("<player>", p.getName()));
	}

}
