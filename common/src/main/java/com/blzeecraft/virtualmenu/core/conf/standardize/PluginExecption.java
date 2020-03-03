package com.blzeecraft.virtualmenu.core.conf.standardize;

public class PluginExecption extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8516136571412240395L;

	public PluginExecption(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public PluginExecption(String msg) {
		super(msg);
	}

	public PluginExecption(Throwable msg) {
		super(msg);
	}
	

}
