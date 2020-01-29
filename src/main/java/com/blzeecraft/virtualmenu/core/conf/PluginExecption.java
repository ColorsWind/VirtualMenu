package com.blzeecraft.virtualmenu.core.conf;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

@SuppressWarnings("serial")
public class PluginExecption extends RuntimeException {

	public PluginExecption(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public PluginExecption(String msg) {
		super(msg);
	}

	public PluginExecption(Throwable msg) {
		super(msg);
	}
	
	public PluginExecption(LogNode node, String msg) {
		super(node.getPrefix() + " " +msg);
	}
	public PluginExecption(LogNode node) {
		super(node.getPrefix() + " ");
	}
	
	public PluginExecption(LogNode node, Throwable throwable, String msg) {
		super(node.getPrefix()  + " " + msg , throwable);
	}

}
