package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;

public interface IFileFormat {
	
	String[] supportTypes();
	
	Map<String, Object> read(LogNode node, Reader reader) throws IOException;
	
	default Map<String, Object> read(LogNode node, File file) throws IOException {
		Reader reader = FileMapFactory.inputFromFile(file);
		val result = read(node, reader);
		reader.close();
		return result;
	}
	
	void write(LogNode node, Writer writer, Map<String, Object> map) throws IOException;
	
	default void write(LogNode node, File file, Map<String, Object> map) throws IOException {
		Writer writer = new FileWriter(file);
		write(node, writer, map);
		writer.close();
	}
	
	
	default String name() {
		return this.getClass().getSimpleName();
	}

}
