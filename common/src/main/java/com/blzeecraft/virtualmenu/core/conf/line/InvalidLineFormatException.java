package com.blzeecraft.virtualmenu.core.conf.line;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;

/**
 * 表示尝试解析一个单行配置(LineConfig)发现语法错误抛出的异常.
 * @author colors_wind
 */
public class InvalidLineFormatException extends ObjectConvertException {

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
