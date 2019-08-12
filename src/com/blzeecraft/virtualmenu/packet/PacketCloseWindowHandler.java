package com.blzeecraft.virtualmenu.packet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;


public class PacketCloseWindowHandler extends PacketAdapter implements Listener {
	private final PacketManager manager;

	public PacketCloseWindowHandler(VirtualMenuPlugin pl, PacketManager manager) {
		super(pl, ListenerPriority.MONITOR, 
				PacketType.Play.Client.CLOSE_WINDOW);
		this.manager = manager;
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		PacketContainer packet = e.getPacket();
		int id = packet.getIntegers().read(0);
		manager.cleanData(e.getPlayer(), id);
	}
	
	@EventHandler
	public void handle(PlayerQuitEvent e) {
		manager.cleanData(e.getPlayer());
	}

}
