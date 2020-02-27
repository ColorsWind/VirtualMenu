package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import lombok.val;

public class PacketWindowItemsHandler extends PacketAdapter {
	
	private final BukkitPlatform platform;
	private final ProtocolLibAdapter packetAdapter;


	public PacketWindowItemsHandler(VirtualMenuPlugin plugin, ProtocolLibAdapter packetAdapter) {
		super(plugin, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS);
		this.platform = plugin.getPlatformAdapter();
		this.packetAdapter = packetAdapter;
	}


	@Override
	public void onPacketSending(PacketEvent event) {
		platform.getUserExact(event.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			val packet = packetAdapter.mapToWindowItems(event.getPacket());
			session.handlePacket(packet);
		});
	}


}
