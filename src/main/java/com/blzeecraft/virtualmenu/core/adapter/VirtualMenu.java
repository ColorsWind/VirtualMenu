package com.blzeecraft.virtualmenu.core.adapter;

import javax.annotation.Nonnull;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.module.PacketHandler;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketSetSlot;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketWindowItems;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketWindowOpen;

import lombok.experimental.UtilityClass;

import net.md_5.bungee.api.chat.BaseComponent;

@UtilityClass
public class VirtualMenu {
	
	public AbstractPacketCloseWindow<?> createPacketCloseWindow(IUser<?> user, int windowId) {
		return packetAdapter.createPacketCloseWindow(user, windowId);
	}

	public AbstractPacketWindowOpen<?> createPacketWindOpen(IUser<?> user, int windowId, IMenuType type, String title) {
		return packetAdapter.createPacketWindOpen(user, windowId, type, title);
	}

	public AbstractPacketSetSlot<?> createPacketSetSlot(IUser<?> user, int windowId, int slot, AbstractItem<?> item) {
		return packetAdapter.createPacketSetSlot(user, windowId, slot, item);
	}

	public AbstractPacketWindowItems<?> createPacketWindowItems(IUser<?> user, int windowId, AbstractItem<?>[] items) {
		return packetAdapter.createPacketWindowItems(user, windowId, items);
	}

	public boolean registerPacketHandler(PacketHandler handler) {
		return packetAdapter.registerPacketHandler(handler);
	}

	public void sendMessage(IUser<?> user, BaseComponent... component) {
		platformAdapter.sendMessage(user, component);
	}

	public void sendMessage(IUser<?> user, String msg) {
		platformAdapter.sendMessage(user, msg);
	}

	public boolean hasPermission(IUser<?> user, String permission) {
		return platformAdapter.hasPermission(user, permission);
	}

	public void sendActionbar(IUser<?> user, String actionbar) {
		platformAdapter.sendActionbar(user, actionbar);
	}

	public void sendTitle(IUser<?> user, String title) {
		platformAdapter.sendTitle(user, title);
	}

	public void sendTitle(IUser<?> user, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		platformAdapter.sendTitle(user, title, subTitle, fadeIn, stay, fadeOut);
	}

	public void performCommand(IUser<?> user, String command) {
		platformAdapter.performCommand(user, command);
	}

	public void performCommandAsAdmin(IUser<?> user, String command) {
		platformAdapter.performCommandAsAdmin(user, command);
	}

	public void performCommandAsConsole(String command) {
		platformAdapter.performCommandAsConsole(command);
	}
	
	public void updateInventory(IUser<?> user) {
		platformAdapter.updateInventory(user);
	}

	private static IPacketAdapter packetAdapter;
	private static IPlatformAdapter platformAdapter;
	
	
	public static void setPacketAdapter(@Nonnull IPacketAdapter adapter) {
		packetAdapter = check("Packetdapter", packetAdapter, adapter);
	}
	
	public static void setPlatformAdapter(@Nonnull IPlatformAdapter adapter) {
		platformAdapter = check("PlatformAdapter", platformAdapter, adapter);
	}
	
	private static <T> T check(@Nonnull String name, T origin, @Nonnull T adapter) {
		if (origin == null) {
			PluginLogger.info(LogNode.ROOT, "已经设置 " + name + " 为 " + adapter.getClass().getTypeName());
			return adapter;
		}
		throw new IllegalArgumentException(name + "已经设置为 " + packetAdapter.getClass().getTypeName() + " 不能再次设置");
		
	}
	



}
