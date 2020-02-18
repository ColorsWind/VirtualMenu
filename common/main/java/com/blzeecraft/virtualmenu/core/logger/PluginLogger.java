package com.blzeecraft.virtualmenu.core.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * 插件日志的工厂类
 * @author colors_wind
 *
 */
@NonNull
@UtilityClass
public class PluginLogger {
	
	private static final Logger LOGGER = Logger.getLogger("VirtualMenu");
	static {
		// minecraft的Logger不会输出INFO以下的信息
		LOGGER.setFilter(record -> {
			if (record.getLevel().intValue() <= Level.INFO.intValue()) {
				record.setLevel(Level.INFO);
			}
			return true;
		});
	}
	
	public static void info(LogNode node, String msg) {
		LOGGER.info(join(node, msg));
	}
	
	private static String join(LogNode node, String msg) {
		return new StringBuilder(node.getPrefix()).append(" ").append(msg).toString();
	}

	public static void fine(LogNode node, String msg) {
		LOGGER.fine(join(node, msg));
	}
	
	public static void finest(LogNode node, String msg) {
		LOGGER.finest(join(node, msg));
	}
	
	public static void severe(LogNode node, String msg) {
		LOGGER.severe(join(node, msg));
	}
	
	
	public static void warning(LogNode node, String msg) {
		LOGGER.warning(join(node, msg));
	}
	
	public static void log(LogNode node, Level level, String msg) {
		LOGGER.log(level, join(node, msg));
	}

}
