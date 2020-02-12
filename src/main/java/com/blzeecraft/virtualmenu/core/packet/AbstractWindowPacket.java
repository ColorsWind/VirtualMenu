package com.blzeecraft.virtualmenu.core.packet;

import lombok.ToString;

/**
 * 代表一个有关window的 Packet.
 * 
 * @author colors_wind
 */
@ToString
public abstract class AbstractWindowPacket<T> extends AbstractPacket<T> {
	
	protected AbstractWindowPacket(T packet) {
		super(packet);
	}

	public abstract void setWindowId(int windowId);
	
	public abstract int getWindowId();

	

}
