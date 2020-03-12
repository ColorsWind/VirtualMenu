package com.blzeecraft.virtualmenu.core.conf;

import com.blzeecraft.virtualmenu.core.PluginExecption;

/**
 * 表示不同类型元素转换时发生的异常. 如尝试将 "string" 转换成 Integer时.
 * 
 * @author colors_wind
 *
 */
public class ObjectConvertException extends PluginExecption {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7796906226004776337L;

	public ObjectConvertException() {
		super();
	}

	public ObjectConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public ObjectConvertException(String message) {
		super(message);
	}

	public ObjectConvertException(Throwable cause) {
		super(cause);
	}
}
