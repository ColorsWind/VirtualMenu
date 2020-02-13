package com.blzeecraft.virtualmenu.core.conf.menu;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.conf.file.FileToMapFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.MapToConfFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.AbstractPacketMenu;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

public class MenuManager {

	public static final ConcurrentMap<String, AbstractPacketMenu> MENU = new ConcurrentHashMap<>();

	public static Optional<AbstractPacketMenu> getMenu(String name) {
		return Optional.ofNullable(MENU.get(name));
	}
	
	public static PacketMenu parse(LogNode node, File file) throws IOException {
		Map<String, Object> map = FileToMapFactory.convert(node, file);
		StandardConf conf = MapToConfFactory.convert(node, map);
		PacketMenu menu = ConfToMenuFactory.convert(node, conf);
		return menu;
	}

	public static PacketMenu parse(LogNode node, String type, Reader reader) throws IOException {
		Map<String, Object> map = FileToMapFactory.convert(node, type, reader);
		StandardConf conf = MapToConfFactory.convert(node, map);
		PacketMenu menu = ConfToMenuFactory.convert(node, conf);
		return menu;
		
	}

}
