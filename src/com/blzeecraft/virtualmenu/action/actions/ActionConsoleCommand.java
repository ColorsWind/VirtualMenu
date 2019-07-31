package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.ToString;

@ToString
public class ActionConsoleCommand extends AbstractAction {

	public ActionConsoleCommand(ILog parent, String raw) {
		super(parent, raw, ActionType.CONSOLE_COMMAND);
	}

	@Override
	public void execute(Player p) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), raw.replace("<player>", p.getName()));
	}

}
