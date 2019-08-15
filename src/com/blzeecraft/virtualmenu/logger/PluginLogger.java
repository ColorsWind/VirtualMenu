package com.blzeecraft.virtualmenu.logger;

import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;

import lombok.Getter;

public class PluginLogger {
	@Getter
	protected static PluginLogger instance;
	
	public static PluginLogger init(VirtualMenuPlugin pl) {
		return instance = new PluginLogger(pl);
	}
	
	public static void info(ILog il, String msg) {
		instance.logger.info(buildMsg(il, msg));
	}
	
	public static void fine(ILog il, String msg) {
		instance.logger.fine(buildMsg(il, msg));
	}
	
	public static void finest(ILog il, String msg) {	
		instance.logger.finest(buildMsg(il, msg));
	}
	
	public static void severe(ILog il, String msg) {
		instance.logger.severe(buildMsg(il, msg));
	}
	
	public static void severe(ILog il, String sub, String msg) {
		instance.logger.severe(buildMsg(il, sub, msg));
	}
	
	
	public static void warning(ILog il, String msg) {
		instance.logger.warning(buildMsg(il, msg));
	}
	
	
	public static void log(ILog il, Level level, String msg) {
		instance.logger.log(level, buildMsg(il, msg));
	}
	
	
	public static String buildMsg(ILog il, String msg) {
		return new StringBuilder(il.getLogPrefix()).append(ILog.END).append(msg).toString();
	}
	
	public static String buildMsg(ILog il, String sub, String msg) {
		return new StringBuilder(il.getLogPrefix()).append(ILog.SPRIT).append(sub).append(ILog.END).append(msg).toString();
	}
	

	protected FileHandler fh;
	private final Logger logger;

	public PluginLogger(VirtualMenuPlugin pl) {
		this.logger = pl.getLogger();
		this.logger.setLevel(Level.FINE);
		logger.setFilter(new Filter() {

			@Override
			public boolean isLoggable(LogRecord record) {
				if (record.getLevel().intValue() <= Level.INFO.intValue()) {
					record.setLevel(Level.INFO);
				}
				return true;
			}
		});
	}
	
	public void addFileHandler() {
		try {
			fh = new FileHandler("./plugins/VirtualMenu/log.txt", true);
			fh.setLevel(Level.FINE);
			fh.setFilter(new Filter() {

				@Override
				public boolean isLoggable(LogRecord record) {
					if (record.getLevel().intValue() <= Level.INFO.intValue()) {
						record.setLevel(Level.INFO);
					}
					return true;
				}});
			logger.addHandler(fh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void unregister() {
		fh.setFilter(null);
		logger.removeHandler(fh);
		logger.setFilter(null);
	}






	
}
