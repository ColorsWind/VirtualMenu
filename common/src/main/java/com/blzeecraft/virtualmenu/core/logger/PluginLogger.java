package com.blzeecraft.virtualmenu.core.logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.blzeecraft.virtualmenu.core.VirtualMenu;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import lombok.experimental.UtilityClass;

/**
 * 插件日志的工厂类
 * @author colors_wind
 *
 */
@NonNull
@UtilityClass
public class PluginLogger {
	
	private static Logger logger = Logger.getLogger("VirtualMenu");
	static {
		// minecraft的Logger不会输出INFO以下的信息
		logger.setFilter(record -> {
			if (record.getLevel().intValue() <= Level.INFO.intValue()) {
				record.setLevel(Level.INFO);
			}
			return true;
		});
	}
	
	@SneakyThrows
	public static void setup() {
		logger = VirtualMenu.getLogger();
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		File logFolder = new File(VirtualMenu.getDataFolder(), "logs");
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}
		File file = new File(logFolder, format.format(d) + ".txt");
		val handler = new FileHandler(file.getPath());
		logger.addHandler(handler);
	}
	
	public static void info(LogNode node, String msg) {
		logger.info(join(node, msg));
	}
	
	private static String join(LogNode node, String msg) {
		return new StringBuilder(node.getPrefix()).append(" ").append(msg).toString();
	}

	public static void fine(LogNode node, String msg) {
		logger.fine(join(node, msg));
	}
	
	public static void finest(LogNode node, String msg) {
		logger.finest(join(node, msg));
	}
	
	public static void severe(LogNode node, String msg) {
		logger.severe(join(node, msg));
	}
	
	
	public static void warning(LogNode node, String msg) {
		logger.warning(join(node, msg));
	}
	
	public static void log(LogNode node, Level level, String msg) {
		logger.log(level, join(node, msg));
	}
	

	public static void debugPacket(Object packet) {
		Class<?> clazz = packet.getClass();
		System.out.print(clazz.getSimpleName());
		Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
			field.setAccessible(true);
			System.out.println(field.getType().getName());
			try {
				System.out.print(field.getGenericType());
			} catch (Exception e) {
			}
			System.out.print(" = ");
			try {
				Object obj = field.get(packet);
				System.out.print(LogFormatter.toString(obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println();
		});
	}

}
