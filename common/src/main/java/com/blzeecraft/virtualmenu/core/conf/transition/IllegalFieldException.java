package com.blzeecraft.virtualmenu.core.conf.transition;

import com.blzeecraft.virtualmenu.core.PluginExecption;

/**
 * 表示负责序列化/反序列化的类字段不合法
 * 
 * @author colors_wind
 *
 */
public class IllegalFieldException extends PluginExecption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1386394777530647500L;

	public IllegalFieldException() {
		super();
	}

	public IllegalFieldException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFieldException(String message) {
		super(message);
	}

	public IllegalFieldException(Throwable cause) {
		super(cause);
	}

}
