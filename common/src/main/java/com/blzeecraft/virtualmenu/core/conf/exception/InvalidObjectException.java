package com.blzeecraft.virtualmenu.core.conf.exception;

/**
 * 尝试读取解析的配置中一个无效的元素(如不存在,类型不正确)时抛出的移除 
 * @author colors_wind
 *
 */
public class InvalidObjectException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = -7796906226004776337L;

	public InvalidObjectException() {
		super();
	}

	public InvalidObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidObjectException(String message) {
		super(message);
	}

	public InvalidObjectException(Throwable cause) {
		super(cause);
	}
}
