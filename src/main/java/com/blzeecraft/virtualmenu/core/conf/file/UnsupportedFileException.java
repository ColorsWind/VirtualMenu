package com.blzeecraft.virtualmenu.core.conf.file;

import com.blzeecraft.virtualmenu.core.conf.standardize.PluginExecption;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

@SuppressWarnings("serial")
public class UnsupportedFileException extends PluginExecption {

	public UnsupportedFileException(LogNode node, String type) {
		super(node, "文件类型 ." + type +" 不被支持");
	}

}
