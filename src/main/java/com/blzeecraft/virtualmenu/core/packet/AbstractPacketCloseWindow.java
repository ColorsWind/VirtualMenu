package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@NonNull
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketCloseWindow<T> extends AbstractPacket<T> {

	protected final int windowId;
	
	/**
	 * 如果该选项为true，拦截此数据包
	 */
	private boolean cancel;

	public AbstractPacketCloseWindow(T handle, IUser<?> user, int windowId) {
		super(handle, user);
		this.windowId = windowId;
	}

}
