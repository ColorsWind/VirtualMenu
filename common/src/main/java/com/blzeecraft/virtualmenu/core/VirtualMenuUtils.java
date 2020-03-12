package com.blzeecraft.virtualmenu.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import lombok.SneakyThrows;
import lombok.val;

public class VirtualMenuUtils {

	@SneakyThrows
	public static InputStream getResource(String name) {
		URL url = VirtualMenuUtils.class.getClassLoader().getResource(name);
		if (url == null) {
			return null;
		}
		URLConnection connection = url.openConnection();
		connection.setUseCaches(false);
		return connection.getInputStream();
	}

	@SneakyThrows
	public static boolean saveResouce(String name, File target) {
		if (!target.exists()) {
			BufferedInputStream in = new BufferedInputStream(getResource(name));
			FileOutputStream out = new FileOutputStream(target);
			byte[] buff = new byte[1024];
			int len;
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		}
		return true;
	}
	
	public static void generateResources(File dataFolder) {
		ensureFolder(new File(dataFolder, "logs"));
		val menuFolder = new File(dataFolder, "menu");
		ensureFolder(menuFolder);
		val menuExample = new File(menuFolder, "example.yml");
		saveResouce("example.yml", menuExample);
		val ccFolder = new File(dataFolder, "chestcommands");
		ensureFolder(ccFolder);
		val ccExample = new File(ccFolder, "exampleCC.yml");
		saveResouce("exampleCC.yml", ccExample);
	}
	
	public static boolean ensureFolder(File folder) {
		if (!folder.exists()) {
			folder.mkdirs();
			return false;
		}
		return true;
	}

}
