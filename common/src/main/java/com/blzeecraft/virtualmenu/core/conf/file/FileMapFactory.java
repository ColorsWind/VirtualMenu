package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import lombok.val;

/**
 * 读取/写入文件的工具类.
 * @author colors_wind
 *
 */
public class FileMapFactory {
	public static final String ENCODE = "utf-8";
	public static final LogNode NODE = LogNode.of("#FileToMapFactory");
	private static final Map<String, IFileFormat> SUPPORT_FORMAT = new HashMap<>();
	static {
		YamlFile yReader = new YamlFile();
		SUPPORT_FORMAT.put("yml", yReader);
		SUPPORT_FORMAT.put("yaml", yReader);
		JsonFile jReader = new JsonFile();
		SUPPORT_FORMAT.put("json", jReader);
	}

	public static void register(IFileFormat reader) {
		for (String type : reader.supportTypes()) {
			type = type.toLowerCase();
			if (SUPPORT_FORMAT.put(type, reader) == null) {
				PluginLogger.info(NODE, "成功添加 ." + type + " 类型文件的解析器.");
			} else {
				PluginLogger.warning(NODE, reader.name() + " 成功覆盖 ." + type + " 类型文件的解析器.");
			}
		}
	}
	
	public static String getExtensionName(File file) {
		String name = file.getName();
		int dot = name.lastIndexOf(".");
		if (dot < 0 || dot == name.length()) {
			return "";
		}
		return name.substring(dot + 1).toLowerCase();
	}
	
	public static String getFileNameNoEx(File file) {
		String name = file.getName();
		int dot = name.lastIndexOf(".");
		return name.substring(0, dot);
	}
	
	public static IFileFormat getFileFormat(File file) {
		return getFileFormat(getExtensionName(file));
	}
	
	public static IFileFormat getFileFormat(String type) {
		if (type == null || type.isEmpty()) {
			throw new UnsupportedFileException("无扩展名");
		}
		val formatter = SUPPORT_FORMAT.get(type.toLowerCase());
		if (formatter == null) {
			throw new UnsupportedFileException(type);
		}
		return formatter;
	}

	public static Map<String, Object> read(LogNode node, File file) throws IOException {
		IFileFormat formatter = getFileFormat(file);
		Reader reader = inputFromFile(file);
		return formatter.read(node, reader);
	}
	
	public static Map<String, Object> read(LogNode node, String type, Reader reader) throws IOException {
		IFileFormat formatter = getFileFormat(type);
		return formatter.read(node, reader);
		
	}
	
	public static void write(LogNode node, File file, Map<String, Object> map) throws IOException {
		IFileFormat formatter = getFileFormat(file);
		Writer writer = outputToFile(file);
		formatter.write(node, writer, map);
	}
	
	public static void write(LogNode node, String type, Writer writer, Map<String, Object> map) throws IOException {
		IFileFormat formatter = getFileFormat(type);
		formatter.write(node, writer, map);
	}
	
	public static boolean vaildFileType(File file) {
		return SUPPORT_FORMAT.containsKey(getExtensionName(file));
	}
	
	public static Reader inputFromFile(File file) throws IOException {
		InputStream ins = new FileInputStream(file);
		Reader reader = new InputStreamReader(ins, ENCODE);
		return reader;
	}
	
	public static Writer outputToFile(File file) throws IOException {
		OutputStream ops = new FileOutputStream(file);
		Writer writer = new OutputStreamWriter(ops, ENCODE);
		return writer;
	}

}
