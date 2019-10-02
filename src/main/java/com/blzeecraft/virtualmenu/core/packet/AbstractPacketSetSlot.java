package com.blzeecraft.virtualmenu.core.packet;


import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表设置菜单中的物品时服务端发送给客户的的数据包
 * @author colors_wind
 *
 * @param <T> 被封装的数据包类型
 */
@NonNull
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketSetSlot<T> extends AbstractPacket<T> {

	protected final int windowId;
	protected final int slot;
	protected final AbstractItem<?> item;
	

	public AbstractPacketSetSlot(T handle, IUser<?> user, int windowId, int slot, AbstractItem<?> item) {
		super(handle, user);
		this.windowId = windowId;
		this.slot = slot;
		this.item = item;
	}
	

}
