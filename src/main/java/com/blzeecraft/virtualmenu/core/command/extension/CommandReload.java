package com.blzeecraft.virtualmenu.core.command.extension;

import com.blzeecraft.virtualmenu.core.command.Callstack;
import com.blzeecraft.virtualmenu.core.command.RequirePermission;
import com.blzeecraft.virtualmenu.core.command.SubCommandBase;
import com.blzeecraft.virtualmenu.core.command.Usage;
import com.blzeecraft.virtualmenu.core.conf.menu.MenuManager;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class CommandReload extends SubCommandBase {
	  
	public CommandReload() {
		super(new String[] { "reload" });
	}
	
	@RequirePermission("virtualmenu.reload")
	@Usage
	public void readPlugin(Callstack stack) {
		IUser<?> user = stack.getSender();
		PacketManager.closeAllMenu();
		MenuManager.reloadMenu();
		user.sendMessageWithPrefix("§a成功重载配置文件.");
	}
}
