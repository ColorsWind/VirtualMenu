package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	public Map<String, Object> convert(File file, LogNode node) throws IOException {
		Yaml yaml = new Yaml();
		InputStream ins = new FileInputStream(file);
		Reader reader = new InputStreamReader(ins, IFileReader.ENCODE);
		Map<String,Object> map = yaml.load(reader);
		return map;
	}

}
