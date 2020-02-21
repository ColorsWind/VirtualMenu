package com.blzeecraft.virtualmenu.core.conf;

/**
 * 表示尝试加载无效的配置时出现的异常.
 * @author colors_wind
 *
 */
public class InvalidConfigException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 2348281412323397238L;

	public InvalidConfigException() {
		super();
	}

	public InvalidConfigException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConfigException(String message) {
		super(message);
	}

	public InvalidConfigException(Throwable cause) {
		super(cause);
	}
	
	

}
