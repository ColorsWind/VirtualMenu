package com.blzeecraft.virtualmenu.core.conf.line;

import com.blzeecraft.virtualmenu.core.conf.InvalidConfigException;

/**
 * 表示尝试解析配置时检查发现元素类型不正确时异常.
 * @author colors_wind
 *
 */
public class InvalidObjectException extends InvalidConfigException {



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
