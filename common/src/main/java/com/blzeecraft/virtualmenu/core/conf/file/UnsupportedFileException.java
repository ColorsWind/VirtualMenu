package com.blzeecraft.virtualmenu.core.conf.file;

import com.blzeecraft.virtualmenu.core.PluginExecption;

public class UnsupportedFileException extends PluginExecption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4254045899401824563L;

	public UnsupportedFileException(String type) {
		super("文件类型 ." + type +" 不被支持");
	}

}
