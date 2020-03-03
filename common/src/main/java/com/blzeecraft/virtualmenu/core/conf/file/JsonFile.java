package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.google.gson.Gson;

public class JsonFile implements IFileFormat {

	@Override
	public String[] supportTypes() {
		return new String[] {"json"};
	}



	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> read(LogNode node, Reader reader) throws IOException {
		Gson gson = new Gson();
		Map<String,Object> map = gson.fromJson(reader, Map.class);
		return map;
	}



	@Override
	public void write(LogNode node, Writer writer, Map<String, Object> map) throws IOException {
		Gson gson = new Gson();
		gson.toJson(map, Map.class, writer);
	}
	


}
