package com.blzeecraft.virtualmenu.core.adapter;

import java.util.Optional;
import java.util.OptionalDouble;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * 服务端平台适配器
 * @author colors_wind
 *
 */
public interface IPlatformAdapter {
	
	
	void sendMessage(IUser<?> user, BaseComponent... component);
	
	void sendMessage(IUser<?> user, String msg);
	
	void sendPluginMessage(IUser<?> user, byte[] byteArray);
	
	int getLevel(IUser<?> user);
	
	void setLevel(IUser<?> user, int level);
	
	OptionalDouble getBalance(IUser<?> user, String currency);
	
	boolean withdraw(IUser<?> user, String currency, double amount);
	
	boolean deposit(IUser<?> user, String currency, double amount);
	
	boolean hasPermission(IUser<?> user, String permission);
	
	void sendActionbar(IUser<?> user, String actionbar);
	
	void sendTitle(IUser<?> user, String title);
	
	void sendTitle(IUser<?> user, String title, String subTitle, int fadeIn, int stay, int fadeOut);

	void playSound(String sound, float volume, float pitch);
	
	boolean performCommand(IUser<?> user, String command);
	
	boolean performCommandAsAdmin(IUser<?> user, String command);
	
	void performCommandAsConsole(String command);
	
	void updateInventory(IUser<?> user);

	AbstractItem<?> emptyItem();
	
	AbstractItemBuilder<?> createItemBuilder();
	
	void runTaskSync(Runnable run);
	
	void runTaskAsync(Runnable run);
	
	AbstractTask<?> runTaskPeriodSync(Runnable run, long delay, long period);
	
	AbstractTask<?> runTaskPeriodAsync(Runnable run, long delay, long period);
	
	Optional<IMenuType> getMenuType(String name);

	IMenuType[] getMenuTypes();

	
	



	
	
}
