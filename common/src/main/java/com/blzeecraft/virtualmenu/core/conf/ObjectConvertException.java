package com.blzeecraft.virtualmenu.core.conf;

import com.blzeecraft.virtualmenu.core.conf.line.InvalidObjectException;

/**
 * 表示 {@link ObjectWrapper} 进行不同类型元素转换时发生的异常.
 * 如将 "String" 转换成数字时.
 * @author colors_wind
 *
 */
public class ObjectConvertException extends InvalidObjectException {

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
