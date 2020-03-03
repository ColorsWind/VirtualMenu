package com.blzeecraft.virtualmenu.core.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.blzeecraft.virtualmenu.core.conf.file.FileAndMapFactory;
import com.blzeecraft.virtualmenu.core.conf.file.YamlFile;
import com.blzeecraft.virtualmenu.core.conf.standardize.MapToConfFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

public class FileToConfTest {
	
	@Test
	public void test() throws IOException {
		File file = new File(".\\src\\main\\resources\\example.yml");
		FileAndMapFactory.register(new YamlFile());
		
		LogNode fNode = LogNode.of(file.getName());
		Map<String, Object> map = FileAndMapFactory.read(fNode, file);
		System.out.println(map);
		LogNode cNode = LogNode.of(file.getName().substring(0, file.getName().lastIndexOf(".")));
		StandardConf conf = MapToConfFactory.read(cNode, map);
		System.out.println(conf);
	}

}
