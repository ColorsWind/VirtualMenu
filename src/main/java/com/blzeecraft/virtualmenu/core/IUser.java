package com.blzeecraft.virtualmenu.core;

import java.util.Optional;
import java.util.OptionalDouble;

import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.module.PacketManager;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * 对玩家进行包装
 * @author colors_wind
 *
 * @param <T> 玩家对应的类型
 */
public interface IUser<T> extends IWrappedObject<T> {

	default void sendMessage(BaseComponent... component) {
		VirtualMenu.sendMessage(this, component);
	}
	
	default void sendMessage(String msg) {
		VirtualMenu.sendMessage(this, msg);
	}
	
	default void sendPluginMessage(String string, byte[] byteArray) {
		VirtualMenu.sendPluginMessage(this, byteArray);
	}
	
	default boolean hasPermission(String permission) {
		return VirtualMenu.hasPermission(this, permission);
	}
	
	default int getLevel() {
		return VirtualMenu.getLevel(this);
	}
	
	default void setLevel(int level) {
		VirtualMenu.setLevel(this, level);
	}
	
	default OptionalDouble getBanlance(String currency) {
		return VirtualMenu.getBalance(this, currency);
	}
	
	default boolean deposit(String currency, double amount) {
		return VirtualMenu.deposit(this, currency, amount);
	}
	
	default boolean withdraw(String currency, double amount) {
		return VirtualMenu.withdraw(this, currency, amount);
	}
	
	default boolean performCommand(String command) {
		return VirtualMenu.performCommand(this, command);
	}
	
	default boolean performCommandAsAdmin(String command) {
		return VirtualMenu.performCommandAsAdmin(this, command);
	}
	
	default void sendActionbar(String actionbar) {
		VirtualMenu.sendActionbar(this, actionbar);
	}
	
	default void sendTitle(String title) {
		VirtualMenu.sendTitle(this, title);
	}
	
	default void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		VirtualMenu.sendTitle(this, title, subTitle, fadeIn, stay, fadeOut);
	}
	
	default void playSound(String sound, float volume, float pitch) {
		VirtualMenu.playSound(sound, volume, pitch);
	}
	
	default void openPacketMenu(IPacketMenu menu) {
		PacketManager.openPacketMenu(this, menu);
	}
	
	default Optional<IPacketMenu> getOpenMenu() {
		return PacketManager.getOpenMenu(this);
	}
	
	default void closePacketMenu(IPacketMenu menu) {
		PacketManager.closePacketMenu(this, menu);
	}
	
	default void closePacketMenu() {
		PacketManager.closePacketMenu(this);
	}

	default void updateInventory() {
		VirtualMenu.updateInventory(this);
	}
	
	PlayerCache getPlayerCache();
	
	void setPlayerCache(PlayerCache cache);

	String getName();


	
	
	
	
	
}
