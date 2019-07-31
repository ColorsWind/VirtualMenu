package com.blzeecraft.virtualmenu.packet;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public abstract class Packet {
	
	public static final ProtocolManager MANAGER = ProtocolLibrary.getProtocolManager();
	protected final PacketContainer packet;
	
	protected Packet(PacketContainer packet) {
		this.packet = packet;
	}
	
	public void send(Player p) throws InvocationTargetException {
		MANAGER.sendServerPacket(p, packet);
	}

}
