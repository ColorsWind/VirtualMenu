package com.blzeecraft.virtualmenu.packet.packets;

import com.blzeecraft.virtualmenu.packet.EnumInventoryType;
import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PacketOpenWindow extends Packet {
	//1.14+
	private static final boolean villageAndPillage;
	
	static {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
		villageAndPillage = packet.getStrings().size() == 0;
	}
	
	public PacketOpenWindow(int windowId, String title, EnumInventoryType type, int slots) {
		super(new PacketContainer(PacketType.Play.Server.OPEN_WINDOW));
		WrappedChatComponent component = WrappedChatComponent.fromText(title);
		packet.getIntegers().write(0, windowId);
		if (villageAndPillage) {
			packet.getIntegers().write(1, type.getId(slots));
		} else {
			packet.getStrings().write(0, type.minecraft());
			packet.getIntegers().write(1, slots);
		}
		packet.getChatComponents().write(0, component);
		
	}
	


}
