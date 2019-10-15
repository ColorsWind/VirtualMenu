package com.blzeecraft.virtualmenu.core.module;

import java.io.Reader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import org.yaml.snakeyaml.Yaml;

import com.blzeecraft.virtualmenu.core.InsensitiveMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileParser {
	
	public static final Function<Reader, Map<String,Object>> YAML = reader -> new Yaml().load(reader); 
	public static final Function<Reader, Map<String,Object>> JSON = reader -> new Gson().fromJson(reader,new TypeToken<Map<String, Object>>(){}.getType());

	public static final ConcurrentMap<String, Function<Reader, Map<String, Object>>> PARSER = InsensitiveMap.wrap(new ConcurrentHashMap<>());
	static {
		PARSER.put("yml", YAML);
		PARSER.put("yaml", YAML);
		PARSER.put("json", JSON);
	}
}
