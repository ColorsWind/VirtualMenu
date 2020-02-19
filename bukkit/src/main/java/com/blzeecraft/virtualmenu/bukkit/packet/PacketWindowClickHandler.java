package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketWindowClickHandler  extends PacketAdapter {
	private final BukkitPlatform platform;

	public PacketWindowClickHandler(VirtualMenuPlugin plugin, PacketManager manager) {
		super(plugin, ListenerPriority.MONITOR, 
				PacketType.Play.Client.WINDOW_CLICK);
		this.platform = plugin.getPlatformAdapter();
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		platform.getUserExact(e.getPlayer()).flatMap(IUser::getCurrentSession).ifPresent(session -> {
			PacketPlayInWindowClick packet = new PacketPlayInWindowClick(e.getPacket());
			PacketManager.map(session, packet).ifPresent(event -> {
				PacketManager.handleEvent(event);
				e.setReadOnly(false);
				e.setCancelled(true);
			});
		
			
		});
		
	}
	

}
