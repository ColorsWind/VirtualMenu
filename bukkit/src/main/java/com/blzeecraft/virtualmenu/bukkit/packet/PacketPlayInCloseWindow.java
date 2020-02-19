package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInCloseWindow;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayInCloseWindow extends AbstractPacketInCloseWindow<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayInCloseWindow(PacketContainer packet) {
		super(packet);
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
