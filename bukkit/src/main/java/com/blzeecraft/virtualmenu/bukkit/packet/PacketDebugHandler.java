package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.BukkitPlatform;
import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketDebugHandler  extends PacketAdapter {
	@SuppressWarnings("unused")
	private final BukkitPlatform platform;

	public PacketDebugHandler(VirtualMenuPlugin plugin) {
		super(plugin, ListenerPriority.MONITOR, 
				PacketType.Play.Server.OPEN_WINDOW, PacketType.Play.Server.WINDOW_ITEMS,  PacketType.Play.Client.CLOSE_WINDOW,  PacketType.Play.Server.SET_SLOT);
		this.platform = plugin.getPlatformAdapter();
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		System.out.println("Receiving packet:" + e.getPacketType() + " from: " + e.getPlayer().getName());
		PluginLogger.debugPacket(e.getPacket().getHandle());
	}

	@Override
	public void onPacketSending(PacketEvent e) {
		System.out.println("Send packet:" + e.getPacketType() + " target: " + e.getPlayer().getName());
		PluginLogger.debugPacket(e.getPacket().getHandle());
	}
	

	
	
	
	
	

}
