package com.blzeecraft.virtualmenu.core.adapter;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import net.md_5.bungee.api.chat.BaseComponent;

public interface IPlatformAdapter {
	
	
	void sendMessage(IUser<?> user, BaseComponent... component);
	
	void sendMessage(IUser<?> user, String msg);
	
	boolean hasPermission(IUser<?> user, String permission);
	
	void sendActionbar(IUser<?> user, String actionbar);
	
	void sendTitle(IUser<?> user, String title);
	
	void sendTitle(IUser<?> user, String title, String subTitle, int fadeIn, int stay, int fadeOut);

	void performCommand(IUser<?> user, String command);
	
	void performCommandAsAdmin(IUser<?> user, String command);
	
	void performCommandAsConsole(String command);
	
	void updateInventory(IUser<?> user);

	AbstractItem<?> emptyItem();
	
	
}
