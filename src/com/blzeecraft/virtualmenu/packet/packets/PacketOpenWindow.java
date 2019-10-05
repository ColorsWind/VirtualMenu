package com.blzeecraft.virtualmenu.packet.packets;

import com.blzeecraft.virtualmenu.packet.MenuType;
import com.blzeecraft.virtualmenu.packet.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PacketOpenWindow extends Packet {
	public static final boolean v1_7;
	public static final boolean v1_14;

	
	static {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
		if(packet.getStrings().size() == 0) {
			v1_14 = true;
			v1_7 = false;
		} else if (packet.getChatComponents().size() == 0) {
			v1_7 = true;
			v1_14 = false;
		} else {
			v1_7 = false;
			v1_14 = false;
		}
	}
	
	public PacketOpenWindow(int windowId, String title, MenuType type) {
		super(new PacketContainer(PacketType.Play.Server.OPEN_WINDOW));
		if (v1_14) {
			this.modifyPacket1_14(windowId, title, type);
		} else if (v1_7) {
			this.modifyPacket1_7(windowId, title, type);
		} else {
			this.modifyPacket1_8(windowId, title, type);
		}
	}
	
	// 临时修复
	public void modifyPacket1_7(int windowId, String title, MenuType type) {
		packet.getStrings().write(0, title);
		packet.getIntegers().write(0, windowId);
		packet.getIntegers().write(1, Math.max(0, type.getLegacyIndex()));
		packet.getIntegers().write(2, type.getSlot());
		packet.getSpecificModifier(boolean.class).write(0, true);
	}
	
	public void modifyPacket1_8(int windowId, String title, MenuType type) {
		WrappedChatComponent component = WrappedChatComponent.fromText(title);
		packet.getIntegers().write(0, windowId);
		packet.getStrings().write(0, type.getMinecraft());
		packet.getIntegers().write(1, type.getSlot());
		packet.getChatComponents().write(0, component);
	}
	
	public void modifyPacket1_14(int windowId, String title, MenuType type) {
		WrappedChatComponent component = WrappedChatComponent.fromText(title);
		packet.getIntegers().write(0, windowId);
		packet.getIntegers().write(1, type.getIndex());
		packet.getChatComponents().write(0, component);
	}


}
