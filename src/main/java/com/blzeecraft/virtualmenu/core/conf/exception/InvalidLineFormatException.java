package com.blzeecraft.virtualmenu.core.conf.exception;

/**
 * 尝试解析一个无效的字符串抛出的异常
 * @author colors_wind
 */
public class InvalidLineFormatException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5048239003927412635L;

	public InvalidLineFormatException() {
		super();
	}

	public InvalidLineFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLineFormatException(String message) {
		super(message);
	}

	public InvalidLineFormatException(Throwable cause) {
		super(cause);
	}

}
