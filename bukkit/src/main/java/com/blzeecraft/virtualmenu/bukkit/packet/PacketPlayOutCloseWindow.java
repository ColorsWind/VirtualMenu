package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutCloseWindow;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutCloseWindow extends AbstractPacketOutCloseWindow<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayOutCloseWindow() {
		super(new PacketContainer(PacketType.Play.Server.CLOSE_WINDOW));
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getIntegers().write(0, windowId);
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

}
