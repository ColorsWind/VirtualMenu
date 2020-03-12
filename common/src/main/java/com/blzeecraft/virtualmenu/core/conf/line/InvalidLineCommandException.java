package com.blzeecraft.virtualmenu.core.conf.line;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;

/**
 * 表示解析单行配置(LineConfig)时命令名不存在的异常.
 * @author colors_wind
 *
 */
public class InvalidLineCommandException extends ObjectConvertException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8773893646529653864L;

	
	public InvalidLineCommandException() {
		super();
	}

	public InvalidLineCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLineCommandException(String message) {
		super(message);
	}

	public InvalidLineCommandException(Throwable cause) {
		super(cause);
	}



}
