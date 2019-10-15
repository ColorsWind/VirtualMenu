package com.blzeecraft.virtualmenu.core.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import com.blzeecraft.virtualmenu.core.config.file.MenuFile;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.AbstractPacketMenu;

import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuManager {

	public static final ConcurrentMap<String, AbstractPacketMenu> MENU = new ConcurrentHashMap<>();

	public static AbstractPacketMenu parse(File file, LogNode node) throws IOException {
		String name = file.getName();
		int index = name.lastIndexOf(".");
		String type;
		if (index < 0) {
			type = "";
		} else {
			type = name.substring(index + 1);
		}
		val in = new FileInputStream(file);
		val reader = new InputStreamReader(in, "utf8");
		return parse(type, reader, node);
	}

	public static AbstractPacketMenu parse(String type, InputStreamReader reader, LogNode node) throws IOException {
		val parser = FileParser.PARSER.get(type);
		if (parser == null) {
			throw new NullPointerException("未识别的文件格式: ." + type + " (没有该读取类型的解析器)");
		}
		return new MenuFile().init(parser.apply(reader)).apply(node);
	}

}
