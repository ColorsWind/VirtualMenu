package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.AbstractItem;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@NonNull
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketWindowItems<T> extends AbstractPacket<T> {

	protected final int windowId;
	protected final AbstractItem<?>[] items;

	public AbstractPacketWindowItems(T handle, IUser<?> user, int windowId, AbstractItem<?>[] items) {
		super(handle, user);
		this.windowId = windowId;
		this.items = items;
	}

}
