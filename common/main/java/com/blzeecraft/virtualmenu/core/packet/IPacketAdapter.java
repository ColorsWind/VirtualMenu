package com.blzeecraft.virtualmenu.core.packet;

import java.lang.reflect.InvocationTargetException;

import com.blzeecraft.virtualmenu.core.user.IUser;

/**
 * Packet 适配器, 用于处理发送 Packet 逻辑.
 * 接收 Packet 的方法没有在这里声明, 需要调用 {@link PacketManager} 的方法进行处理. 
 * @author colors_wind
 * @see {@link PacketManager#handleEvent(PacketMenuClickEvent)}
 * @see {@link PacketManager#handleEvent(PacketMenuCloseEvent)}
 *
 */
public interface IPacketAdapter {
	

	AbstractPacketOutCloseWindow<?> createPacketCloseWindow();

	AbstractPacketOutWindowOpen<?> createPacketWindOpen();

	AbstractPacketOutSetSlot<?> createPacketSetSlot();

	AbstractPacketOutWindowItems<?> createPacketWindowItems();

	void sendServerPacket(IUser<?> user, AbstractWindowPacket<?> packet) throws InvocationTargetException;
	
	
}
