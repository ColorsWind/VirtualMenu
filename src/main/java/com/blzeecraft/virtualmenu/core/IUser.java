package com.blzeecraft.virtualmenu.core;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.module.PacketManager;

import net.md_5.bungee.api.chat.BaseComponent;

public interface IUser<T> extends IWrappedObject<T> {

	default void sendMessage(BaseComponent... component) {
		VirtualMenu.sendMessage(this, component);
	}
	
	default void sendMessage(String msg) {
		VirtualMenu.sendMessage(this, msg);
	}
	
	default boolean hasPermission(String permission) {
		return VirtualMenu.hasPermission(this, permission);
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
	
	
	
}
