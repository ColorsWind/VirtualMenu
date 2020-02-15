package com.blzeecraft.virtualmenu.core.command.extension;

import com.blzeecraft.virtualmenu.core.command.Callstack;
import com.blzeecraft.virtualmenu.core.command.SubCommandBase;
import com.blzeecraft.virtualmenu.core.command.Usage;
import com.blzeecraft.virtualmenu.core.conf.menu.MenuManager;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class CommandList extends SubCommandBase {

	public CommandList() {
		super(new String[] { "list" });
	}

	@Usage("§aopen §c-§a 列出可用的菜单")
	public boolean listMenu(Callstack stack) {
		IUser<?> sender = stack.getSender();
		sender.sendMessageWithPrefix("§a可用的菜单列表： ");
		MenuManager.getMenusName()
				.forEach(name -> sender.sendMessage(new StringBuilder("§c- §e").append(name).toString()));
		return true;
	}

}
