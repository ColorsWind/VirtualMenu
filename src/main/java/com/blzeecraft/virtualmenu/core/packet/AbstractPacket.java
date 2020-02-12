package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * 代表一个 Packet.
 * 
 * @param <T> 被封装的关于Window的 Packet 的类型
 * @author colors_wind
 * @date 2020-02-12
 */
@ToString
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPacket<T> implements IWrappedObject<T> {
	protected final T packet;
	
	
	@Override
	public T getHandle() {
		return packet;
	}

}
