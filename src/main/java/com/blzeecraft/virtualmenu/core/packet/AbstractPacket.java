package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表一个数据包
 * @author colors_wind
 *
 * @param <T> 被封装的数据包类型
 */
@NonNull
@ToString
@Getter
@AllArgsConstructor
public abstract class AbstractPacket<T> implements IWrappedObject<T> {
	protected final T handle;
	protected final IUser<?> user;
	
	public abstract boolean send();
	

}
