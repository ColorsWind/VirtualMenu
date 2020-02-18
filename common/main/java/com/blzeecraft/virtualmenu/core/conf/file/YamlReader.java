package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

public class YamlReader implements IFileReader {

	@Override
	public String[] supportTypes() {
		return new String[] {"yml", "yaml"};
	}

	@Override
	public Map<String, Object> convert(LogNode node, Reader reader) throws IOException {
		Yaml yaml = new Yaml();
		Map<String,Object> map = yaml.load(reader);
		return map;
	}

}
