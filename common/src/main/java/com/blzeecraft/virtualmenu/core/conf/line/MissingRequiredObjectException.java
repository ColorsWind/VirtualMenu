package com.blzeecraft.virtualmenu.core.conf.line;

import com.blzeecraft.virtualmenu.core.conf.InvalidConfigException;

/**
 * 表示解析配置文件时检查发现带读取的必须元素不存在时抛出的异常.
 * @author colors_wind
 *
 */
public class MissingRequiredObjectException extends InvalidConfigException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6365124405281241184L;

	public MissingRequiredObjectException() {
		super();
	}

	public MissingRequiredObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingRequiredObjectException(String message) {
		super(message);	
	}

	public MissingRequiredObjectException(Throwable cause) {
		super(cause);
	}

}
