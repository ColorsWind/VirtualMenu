package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketDebugHandler  extends PacketAdapter {
	@SuppressWarnings("unused")
	private final BukkitPlatform platform;

	public PacketDebugHandler(VirtualMenuPlugin plugin) {
		super(plugin, ListenerPriority.MONITOR, 
				PacketType.Play.Server.OPEN_WINDOW, PacketType.Play.Server.WINDOW_ITEMS,  PacketType.Play.Client.CLOSE_WINDOW);
		this.platform = plugin.getPlatformAdapter();
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		System.out.println(e.toString());
	}

	@Override
	public void onPacketSending(PacketEvent e) {
		System.out.println(e.toString());
	}
	
	
	

}
