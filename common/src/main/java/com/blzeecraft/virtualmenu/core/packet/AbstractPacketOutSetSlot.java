package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.ToString;

/**
 * 代表设置菜单单个 Slot 时服务端发送给客户端的 Packet.
 * 
 * @author colors_wind
 *
 */

@ToString(callSuper = true)
public abstract class AbstractPacketOutSetSlot<T> extends AbstractWindowPacket<T> {

	public AbstractPacketOutSetSlot(T handle) {
		super(handle);
	}

	public abstract void setSlot(int slot);

	public abstract int getSlot();

	public abstract void setItem(AbstractItem<?> item);

	public abstract AbstractItem<?> getItem();

}
