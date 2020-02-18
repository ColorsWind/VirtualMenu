package com.blzeecraft.virtualmenu.core.command;

/**
 * 表示解析命令参数时出现异常.
 * @author colors_wind
 * @date 2020-02-15
 */
public class IllegalCommandArgumentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1265652245261882178L;

	public IllegalCommandArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalCommandArgumentException(String message) {
		super(message);
	}

	public String format(int index, String argument) {
		return this.getMessage().replace("%index%", String.valueOf(index)).replace("%arg%", argument);
	}

}
