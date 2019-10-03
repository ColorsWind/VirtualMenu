package com.blzeecraft.virtualmenu.core.config;

/**
 * 尝试读取解析的配置中一个无效的元素(如不存在,类型不正确)时抛出的移除 
 * @author colors_wind
 *
 */
public class InvalidLineObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6803138369380235644L;

	public InvalidLineObjectException() {
		super();
	}

	public InvalidLineObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLineObjectException(String message) {
		super(message);
	}

	public InvalidLineObjectException(Throwable cause) {
		super(cause);
	}
}
