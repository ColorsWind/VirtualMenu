package com.blzeecraft.virtualmenu.core;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

/**
 * 代表 VirtualMenu 产生的异常.
 * @author colors_wind
 *
 */
public class PluginExecption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8516136571412240395L;


	public PluginExecption() {
		super();
	}


	public PluginExecption(String message, Throwable cause) {
		super(message, cause);
	}


	public PluginExecption(String message) {
		super(message);
	}


	public PluginExecption(Throwable cause) {
		super(cause);
	}


	public void printStackTrace(LogNode node) {
		PluginLogger.severe(node, "捕获到异常: " + this.getClass().getSimpleName());
		this.printStackTrace();
	}
	
	
	

}
