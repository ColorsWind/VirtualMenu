package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInWindowClick;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketWindowClickHandler  extends PacketAdapter {
	private final BukkitPlatform platform;
	private final ProtocolLibAdapter packetAdapter;

	public PacketWindowClickHandler(VirtualMenuPlugin plugin, ProtocolLibAdapter packetAdapter) {
		super(plugin, ListenerPriority.MONITOR, 
				PacketType.Play.Client.WINDOW_CLICK);
		this.platform = plugin.getPlatformAdapter();
		this.packetAdapter = packetAdapter;
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			AbstractPacketInWindowClick<?> packet = packetAdapter.mapToWindowClick(e.getPacket());
			PacketManager.map(session, packet).ifPresent(event -> {
				PacketManager.handleEvent(event);
				e.setReadOnly(false);
				e.setCancelled(true);
			});
		
			
		});
		
	}
	

}
