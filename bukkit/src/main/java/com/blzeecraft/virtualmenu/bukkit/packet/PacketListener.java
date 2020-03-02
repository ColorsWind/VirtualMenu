package com.blzeecraft.virtualmenu.bukkit.packet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.packet.PacketHandler;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import lombok.val;

public class PacketListener extends PacketAdapter implements Listener {
	private final BukkitPlatform platform;
	private final ProtocolLibAdapter packetAdapter;

	public PacketListener(VirtualMenuPlugin plugin, ProtocolLibAdapter packetAdapter) {
		super(plugin, ListenerPriority.MONITOR, PacketType.Play.Client.CLOSE_WINDOW,
				PacketType.Play.Client.WINDOW_CLICK, PacketType.Play.Server.WINDOW_ITEMS,
				PacketType.Play.Server.SET_SLOT);
		this.platform = plugin.getPlatformAdapter();
		this.packetAdapter = packetAdapter;
	}

	@Override
	public void onPacketReceiving(PacketEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			val packetType = e.getPacketType();
			val packet = e.getPacket();
			final boolean cancel;
			if (packetType == PacketType.Play.Client.CLOSE_WINDOW) {
				cancel = PacketHandler.handleClientPacket(session, packetAdapter.mapToCloseWindow(packet));
			} else if (packetType == PacketType.Play.Client.WINDOW_CLICK) {
				cancel = PacketHandler.handleClientPacket(session, packetAdapter.mapToWindowClick(packet));
			} else {
				return;
			}
			if (cancel) {
				e.setReadOnly(false);
				e.setCancelled(true);
			}
		});

	}

	@Override
	public void onPacketSending(PacketEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			val packetType = e.getPacketType();
			val packet = e.getPacket();
			final boolean cancel;
			if (packetType == PacketType.Play.Server.WINDOW_ITEMS) {
				cancel = PacketHandler.handleServerPacket(session, packetAdapter.mapToWindowItems(packet));
			} else if (packetType == PacketType.Play.Client.WINDOW_CLICK) {
				cancel = PacketHandler.handleServerPacket(session, packetAdapter.mapToSetSlot(packet));
			} else {
				return;
			}
			if (cancel) {
				e.setReadOnly(false);
				e.setCancelled(true);
			}
		});
	}

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession)
				.ifPresent(UserManager::handleClosePacketMenu);
	}

}
