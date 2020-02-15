package com.blzeecraft.virtualmenu.core.command.extension;

import com.blzeecraft.virtualmenu.core.command.Callstack;
import com.blzeecraft.virtualmenu.core.command.PlayerOnly;
import com.blzeecraft.virtualmenu.core.command.RequirePermission;
import com.blzeecraft.virtualmenu.core.command.SubCommandBase;
import com.blzeecraft.virtualmenu.core.command.Usage;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.user.IUser;

public class CommandOpenMenu extends SubCommandBase {

	public CommandOpenMenu() {
		super(new String[] {"open"});
	}
	

	@PlayerOnly
	@RequirePermission("virtualmenu.open.self")
	@Usage("§aopen §c-§a [menu] <player> 为打开菜单")
	public boolean openMenu(Callstack stack, IPacketMenu menu) {
		IUser<?> sender = stack.getSender();
		sender.openPacketMenu(menu);
		sender.sendMessageWithPrefix("§a成功为自己打开菜单 ", stack.getArgs()[1], ".");
		return true;
	}
	
	@RequirePermission("virtualmenu.open.others")
	@Usage
	public boolean openMenu(Callstack stack, IPacketMenu menu, IUser<?> user) {
		IUser<?> sender = stack.getSender();
		sender.sendMessageWithPrefix("§a成功为 ", user.getName() ," 打开菜单 ", stack.getArgs()[1], ".");
		user.openPacketMenu(menu);
		return true;
	}
}
