package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

public class FileToMapFactory {
	public static final String ENCODE = "utf-8";
	public static final LogNode NODE = LogNode.of("#FileToMapFactory");
	public static final Map<String, IFileReader> SUPPORT_READER = new HashMap<>();
	static {
		YamlReader yReader = new YamlReader();
		SUPPORT_READER.put("yml", yReader);
		SUPPORT_READER.put("yaml", yReader);
		JsonReader jReader = new JsonReader();
		SUPPORT_READER.put("json", jReader);
	}

	public static void register(IFileReader reader) {
		for (String type : reader.supportTypes()) {
			type = type.toLowerCase();
			if (SUPPORT_READER.putIfAbsent(type, reader) == null) {
				PluginLogger.info(NODE, "成功添加 ." + type + " 类型文件的支持.");
			} else {
				PluginLogger.warning(NODE, reader.name() + " 尝试覆盖 ." + type + " 类型文件读取设置失败.");
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

	public static Map<String, Object> convert(LogNode node, File file) throws IOException {
		String type = getExtensionName(file);
		if (type.isEmpty()) {
			throw new UnsupportedFileException(node, "无扩展名");
		}
		IFileReader reader = SUPPORT_READER.get(type);
		if (reader == null) {
			throw new UnsupportedFileException(node, type);
		}
		return reader.convert(node, file);
	}
	
	public static Map<String, Object> convert(LogNode node, String type, Reader reader) throws IOException {
		IFileReader fReader = SUPPORT_READER.get(type);
		if (reader == null) {
			throw new UnsupportedFileException(node, type);
		}
		return fReader.convert(node, reader);
		
	}
	
	public static boolean vaildFileType(File file) {
		return SUPPORT_READER.containsKey(getExtensionName(file));
	}
	
	public static Reader inputFromFile(File file) throws IOException {
		InputStream ins = new FileInputStream(file);
		Reader reader = new InputStreamReader(ins, ENCODE);
		return reader;
	}

}
