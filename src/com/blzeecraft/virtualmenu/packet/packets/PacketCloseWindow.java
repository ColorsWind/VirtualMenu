package com.blzeecraft.virtualmenu.packet.packets;

import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketCloseWindow extends Packet {

	public PacketCloseWindow(int windowId) {
		super(new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW));
		packet.getIntegers().write(0, windowId);
	}

}
