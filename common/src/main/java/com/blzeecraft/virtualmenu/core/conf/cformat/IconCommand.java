package com.blzeecraft.virtualmenu.core.conf.cformat;


import com.blzeecraft.virtualmenu.core.action.extension.ActionBungeeCord;
import com.blzeecraft.virtualmenu.core.action.extension.ActionCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpCommand;
import com.blzeecraft.virtualmenu.core.action.extension.ActionOpenMenu;
import com.blzeecraft.virtualmenu.core.action.extension.ActionSound;
import com.blzeecraft.virtualmenu.core.action.extension.ActionTell;

import lombok.val;
import lombok.var;

public class IconCommand {
	
	public static String remap(String name) {
		int index = name.indexOf(':');
		if (index < 0 || index >= name.length() ) {
			return ActionCommand.remap(name);
		}
		val key = name.substring(0, index).toLowerCase();
		var value = name.substring(index + 1);
		if (value.startsWith(" ")) {
			value = value.substring(1);
		}
		switch(key) {
		case "console":
			return ActionConsoleCommand.remap(value);
		case "op":
			return ActionOpCommand.remap(value);
		case "open":
		case "menu":
			return ActionOpenMenu.remap(value);
		case "server":
			return ActionBungeeCord.remap(value);
		case "tell":
			return ActionTell.remap(value);
		case "sound":
			return ActionSound.remap(value);
		default:
			return "console{command=say 不支持ChestCommands命令类型 " + value + " \\,请检查.}";	
		}
	}

}
