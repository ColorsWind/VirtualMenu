package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表一个 Packet
 * 
 * @author colors_wind
 * @param <T> 被封装的关于Window的 Packet 的类型
 */
@NonNull
@ToString
@Getter
@AllArgsConstructor
public abstract class AbstractWindowPacket<T> implements IWrappedObject<T> {
	protected final T packet;
	
	public abstract void setWindowId(int windowId);
	
	public abstract int getWindowId();

	

}
