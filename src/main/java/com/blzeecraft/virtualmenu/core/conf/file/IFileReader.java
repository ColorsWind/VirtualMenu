package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

public interface IFileReader {
	public static final String ENCODE = "utf-8";
	
	String[] supportTypes();
	
	Map<String, Object> convert(File file, LogNode node) throws IOException;
	
	default String name() {
		return this.getClass().getSimpleName();
	}

}
