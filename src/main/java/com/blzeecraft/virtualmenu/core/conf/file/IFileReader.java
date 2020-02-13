package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

public interface IFileReader {
	
	String[] supportTypes();
	
	Map<String, Object> convert(LogNode node, Reader reader) throws IOException;
	
	default Map<String, Object> convert(LogNode node, File file) throws IOException {
		Reader reader = FileToMapFactory.inputFromFile(file);
		return convert(node, reader);
	}
	
	
	default String name() {
		return this.getClass().getSimpleName();
	}

}
