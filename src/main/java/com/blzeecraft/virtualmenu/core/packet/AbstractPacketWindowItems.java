package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.ToString;

/**
 * 代表更新菜单 Slot 时服务端发送给客户端的 Packet.
 * @author colors_wind
 *
 */
@ToString(callSuper = true)
public abstract class AbstractPacketWindowItems<T> extends AbstractWindowPacket<T> {

	public AbstractPacketWindowItems(T handle) {
		super(handle);
	}

	public abstract void setItems(AbstractItem<?>[] items);

	public abstract AbstractItem<?>[] getItems();

}
