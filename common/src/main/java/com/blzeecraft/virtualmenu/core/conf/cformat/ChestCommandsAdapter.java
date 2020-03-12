package com.blzeecraft.virtualmenu.core.conf.cformat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.conf.file.FileAndMapFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;

public class ChestCommandsAdapter {
	public static final LogNode LOG_NODE = LogNode.of("ChestCommandsAdapter");
	
	public static StandardConf load(File file) throws IOException {
			val map = FileAndMapFactory.read(LOG_NODE, file);
			val node = LogNode.of(String.join("|", "cc", FileAndMapFactory.getFileNameNoEx(file)));
			val conf = load(node, map);
			return conf;
	}

	public static StandardConf load(LogNode node, Map<String, Object> map) {
		return new ChestCommandsConf(node, map).remap();
	}

}
