package com.blzeecraft.virtualmenu.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.blzeecraft.virtualmenu.core.conf.standardize.MapToConfFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

public class StandardMenuFileTest {
	
	@Test
	public void testConvert() throws Exception {
		File file = new File(".\\resources\\example.yml");
		Yaml yaml = new Yaml();
		InputStream ins = new FileInputStream(file);
		Reader reader = new InputStreamReader(ins, "utf-8");
		Map<String,Object> map = yaml.load(reader);
		StandardConf menu = MapToConfFactory.convert(LogNode.ROOT, map);
		System.out.println(menu.toString());
	}

}
