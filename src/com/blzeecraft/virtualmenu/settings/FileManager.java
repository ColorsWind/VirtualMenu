package com.blzeecraft.virtualmenu.settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.logger.PluginLogger;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class FileManager {
	@Getter
	private static FileManager instance;

	public static FileManager init(VirtualMenuPlugin pl) {
		return instance = new FileManager(pl);
	}

	private VirtualMenuPlugin pl;
	protected File dataFolder;
	protected File menusFolder;
	protected File configFile;
	protected File exampleFile;
	protected File boundFile;
	
	public FileManager(VirtualMenuPlugin pl) {
		this.pl = pl;
	}

	public boolean generate() {
		dataFolder = pl.getDataFolder();
		menusFolder = new File(dataFolder, "menu");
		if (!dataFolder.exists()) {
			dataFolder.mkdir();
			PluginLogger.fine(pl, "mkdir "+ dataFolder.getAbsolutePath());
		}
		if (!menusFolder.exists()) {
			menusFolder.mkdir();
			PluginLogger.fine(pl, "mkdir " + menusFolder.getAbsolutePath());
		}
		configFile = new File(dataFolder, "config.yml");
		if (!configFile.exists()) {
			pl.saveDefaultConfig();
			PluginLogger.fine(pl, "save " + configFile.getAbsolutePath());
		}
		boundFile = new File(dataFolder, "bound.yml");
		if (!boundFile.exists()) {
			saveFile("bound.yml", boundFile);
		}
		exampleFile = new File(menusFolder, "example.yml");
		if (! exampleFile.exists()) {
			saveFile("example.yml", exampleFile);
		}
		return true;
	}
	
	@SneakyThrows
	public boolean saveFile(String name, File target) {
		InputStream in = pl.getResource(name);
		OutputStream out = new FileOutputStream(target);
        byte[] buffer = new byte[1024]; // 1KB
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);   
        }
        out.close();
        PluginLogger.fine(pl, "save " + target.getAbsolutePath());
        return true;
	}


}
