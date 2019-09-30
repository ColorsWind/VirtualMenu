package com.blzeecraft.virtualmenu.core.packet;

import javax.annotation.Nonnull;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Nonnull
@ToString
@Getter
@AllArgsConstructor
public abstract class AbstractPacket<T> implements IWrappedObject<T> {
	protected final T handle;
	protected final IUser<?> user;
	
	public abstract boolean send();
	

}
