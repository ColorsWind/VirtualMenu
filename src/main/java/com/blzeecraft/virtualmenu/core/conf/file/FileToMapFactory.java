package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

public class FileToMapFactory {
	public static final LogNode NODE = LogNode.of("#FileToMapFactory");
	public static final Map<String, IFileReader> SUPPORT_READER = new HashMap<>();

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

	public static Map<String, Object> convert(LogNode node, File file) throws IOException {
		String name = file.getName();
		int dot = name.lastIndexOf(".");
		if (dot < 0 || dot == name.length()) {
			throw new UnsupportedFileException(node, "无扩展名");
		}
		String type = name.substring(dot + 1).toLowerCase();
		IFileReader reader = SUPPORT_READER.get(type);
		if (reader == null) {
			throw new UnsupportedFileException(node, type);
		}
		return reader.convert(file, node);
	}

}
