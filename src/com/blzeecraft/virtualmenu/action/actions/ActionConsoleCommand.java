package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;

@ToString
public class ActionConsoleCommand extends AbstractAction {

	public ActionConsoleCommand(ILog parent, String raw) {
		super(parent, raw, ActionType.CONSOLE_COMMAND);
	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		String text = raw;
		if (isPlaceholderAPI) {
			text = PlaceholderAPI.setPlaceholders(p, text);
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text.replace("<player>", p.getName()));
	}

}
