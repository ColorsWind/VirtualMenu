package com.blzeecraft.virtualmenu.core.conf.line;

import com.blzeecraft.virtualmenu.core.conf.InvalidConfigException;

/**
 * 表示尝试解析一个命令(LineCommand)个数不正确时出现的异常.
 * @author colors_wind
 */
public class InvalidLineFormatException extends InvalidConfigException {

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
