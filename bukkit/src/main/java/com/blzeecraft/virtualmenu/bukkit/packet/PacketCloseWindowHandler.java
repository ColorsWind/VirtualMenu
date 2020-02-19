package com.blzeecraft.virtualmenu.bukkit.packet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.packet.PacketMenuCloseEvent;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketCloseWindowHandler extends PacketAdapter implements Listener {
	private final BukkitPlatform platform;

	public PacketCloseWindowHandler(VirtualMenuPlugin plugin) {
		super(plugin, ListenerPriority.MONITOR, PacketType.Play.Client.CLOSE_WINDOW);
		this.platform = plugin.getPlatformAdapter();
	}

	@Override
	public void onPacketReceiving(PacketEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			PacketPlayInCloseWindow packet = new PacketPlayInCloseWindow(e.getPacket());
			PacketManager.map(session, packet).ifPresent(PacketManager::handleEvent);
		});
	}

	@EventHandler
	public void handle(PlayerQuitEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession)
				.map(session -> new PacketMenuCloseEvent(session, true)).ifPresent(PacketManager::handleEvent);
	}

}
