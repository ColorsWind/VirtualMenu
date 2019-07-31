package com.blzeecraft.virtualmenu.action;

import com.blzeecraft.virtualmenu.logger.ILog;

public interface ActionExecutor<T extends AbstractAction> {

	public T fromString(ILog il, String raw);
}
