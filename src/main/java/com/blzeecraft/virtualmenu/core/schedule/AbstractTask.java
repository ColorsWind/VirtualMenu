package com.blzeecraft.virtualmenu.core.schedule;

import com.blzeecraft.virtualmenu.core.IWrappedObject;

public abstract class AbstractTask<T> implements IWrappedObject<T> {
	
	protected final T handle;

	public AbstractTask(T handle) {
		this.handle = handle;
	}

	@Override
	public T getHandle() {
		return handle;
	}
	
	public abstract boolean isRunning();
	
	public abstract boolean isAsync();
	
	public abstract void cancel();

}
