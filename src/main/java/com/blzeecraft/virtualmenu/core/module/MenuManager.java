package com.blzeecraft.virtualmenu.core.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import com.blzeecraft.virtualmenu.core.menu.AbstractPacketMenu;

import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuManager {

	private static final ConcurrentMap<String, Function<Reader, AbstractPacketMenu>> PARSER = new ConcurrentHashMap<>();
	private static final ConcurrentMap<String, AbstractPacketMenu> MENU = new ConcurrentHashMap<>();

	public static boolean addMenu(String name, AbstractPacketMenu menu) {
		return MENU.putIfAbsent(name, menu) == null;
	}
	
	public static Optional<AbstractPacketMenu> getMenu(String name) {
		return Optional.ofNullable(MENU.get(name));
	}

	public static boolean removeMenu(String name) {
		return MENU.remove(name) != null;
	}

	public static void clearMenus() {
		MENU.clear();
	}

	public static Collection<AbstractPacketMenu> getMenus() {
		return MENU.values();
	}

	public static boolean addParser(String type, Function<Reader, AbstractPacketMenu> parser) {
		return PARSER.putIfAbsent(type, parser) == null;
	}
	
	public static boolean removeParser(String type) {
		return PARSER.remove(type) != null;
	}
	public static Optional<Function<Reader, AbstractPacketMenu>> getParser(String type) {
		return Optional.ofNullable(PARSER.get(type));
	}

	public static AbstractPacketMenu parse(File file) throws IOException {
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
		return parse(type, reader);
	}

	public static AbstractPacketMenu parse(String type, InputStreamReader reader) throws IOException {
		val parser = PARSER.get(type);
		if (parser == null) {
			throw new NullPointerException("未识别的文件格式: ." + type + " (没有该读取类型的解析器)");
		}
		return parser.apply(reader);
	}

}
