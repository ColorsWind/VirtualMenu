package com.blzeecraft.virtualmenu.packet.packets;

import com.blzeecraft.virtualmenu.packet.EnumInventoryType;
import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PacketOpenWindow extends Packet {
	
	public PacketOpenWindow(int windowId, String title, EnumInventoryType type, int slots) {
		super(new PacketContainer(PacketType.Play.Server.OPEN_WINDOW));
		WrappedChatComponent component = WrappedChatComponent.fromText(title);
		packet.getIntegers().write(0, windowId);
		packet.getStrings().write(0, type.minecraft());
		packet.getChatComponents().write(0, component);
		packet.getIntegers().write(1, slots);
	}

}
