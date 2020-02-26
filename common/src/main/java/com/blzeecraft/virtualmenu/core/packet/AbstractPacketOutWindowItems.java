package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.ToString;

/**
 * 代表更新菜单 Slot 时服务端发送给客户端的 Packet.
 * @author colors_wind
 *
 */
@ToString(callSuper = true)
public abstract class AbstractPacketOutWindowItems<T> extends AbstractWindowPacket<T> {

	public AbstractPacketOutWindowItems(T handle) {
		super(handle);
	}

	public abstract void setItems(AbstractItem<?>[] items);
	
	public abstract void setRawItems(Object[] items);

	public abstract AbstractItem<?>[] getItems();
	
	public abstract Object[] getRawItems();

}
