package com.blzeecraft.virtualmenu.core.packet;


import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.AbstractItem;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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
