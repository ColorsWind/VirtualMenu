package com.blzeecraft.virtualmenu.action;

import java.util.StringTokenizer;

import org.bukkit.Bukkit;

import com.blzeecraft.virtualmenu.action.actions.ActionActionbar;
import com.blzeecraft.virtualmenu.action.actions.ActionBungeeCord;
import com.blzeecraft.virtualmenu.action.actions.ActionCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionOpCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionOpenMenu;
import com.blzeecraft.virtualmenu.action.actions.ActionSound;
import com.blzeecraft.virtualmenu.action.actions.ActionTell;
import com.blzeecraft.virtualmenu.action.actions.ActionTitle;
import com.blzeecraft.virtualmenu.event.CustomActionLoadEvent;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;

import lombok.Getter;

public class ActionManager {
	
	@Getter
	protected static ActionManager instance;
	
	public static ActionManager init() {
		return instance = new ActionManager();
	}
	
	public AbstractAction fromString(ILog parent, String s) {
		StringTokenizer str = new StringTokenizer(s, ":");
		String prefix = str.nextToken();
		if (str.hasMoreTokens()) {
			String raw = str.nextToken();
			if (raw.startsWith(" ")) {
				raw = raw.substring(1);
			}
			AbstractAction action = getAction(prefix.toLowerCase(), raw, parent);
			if (action != null) {
				return action;
			}
		}
		return new ActionCommand(parent, s);
	}

	public AbstractAction getAction(String prefix, String raw, ILog il) {
		switch(prefix) {
		case"":
			return new ActionCommand(il, raw);
		case"op":
			return new ActionOpCommand(il, raw);
		case"console":
			return new ActionConsoleCommand(il, raw);
		case"tell":
			return new ActionTell(il, raw);
		case"actionbar":
			return new ActionActionbar(il, raw);
		case"title":
			return new ActionTitle(il, raw);
		case"sound":
			return new ActionSound(il, raw);
		case"open":
			return new ActionOpenMenu(il, raw);
		case"server":
			return new ActionBungeeCord(il, raw);
		default:
			CustomActionLoadEvent event = new CustomActionLoadEvent(prefix, raw);
			PluginLogger.finest(il, "尝试注册自定义动作: " + prefix + ":" + raw);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isRegister()) {
				PluginLogger.finest(il, "成功注册自定义动作: " + prefix + ":" + raw);
				return event.getAction();
			}
			return null;
		}
	}


	

}
