package com.blzeecraft.virtualmenu.core.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.blzeecraft.virtualmenu.core.conf.file.FileToMapFactory;
import com.blzeecraft.virtualmenu.core.conf.file.YamlReader;
import com.blzeecraft.virtualmenu.core.conf.menu.ConfToMenuFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.MapToConfFactory;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

public class IconConfTest {
	
	@Test
	public void test() throws IOException {
		File file = new File(".\\src\\main\\resources\\example.yml");
		FileToMapFactory.register(new YamlReader());
		
		LogNode fNode = LogNode.of(file.getName());
		Map<String, Object> map = FileToMapFactory.convert(fNode, file);
		System.out.println("Map=" + map + "\n\n");
		
		String name = file.getName().substring(0, file.getName().lastIndexOf("."));
		LogNode cNode = LogNode.of(name);
		StandardConf conf = MapToConfFactory.convert(cNode, map);
		System.out.println("Conf=" + conf + "\n\n");
		
		//事实上没办法通过测试,因为缺少平台适配器
		PacketMenu menu = ConfToMenuFactory.convert(cNode, conf);
		System.out.println("Menu=" + menu + "\n\n");
	}

}
