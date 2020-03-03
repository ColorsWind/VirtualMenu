package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

public class YamlFile implements IFileFormat {

	@Override
	public String[] supportTypes() {
		return new String[] {"yml", "yaml"};
	}

	@Override
	public Map<String, Object> read(LogNode node, Reader reader) throws IOException {
		Yaml yaml = new Yaml();
		Map<String,Object> map = yaml.load(reader);
		return map;
	}

	@Override
	public void write(LogNode node, Writer writer, Map<String, Object> map) throws IOException {
		Yaml yaml = new Yaml();
		yaml.dump(map, writer);
	}

}
