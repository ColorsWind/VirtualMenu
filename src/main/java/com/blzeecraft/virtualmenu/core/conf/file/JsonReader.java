package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.google.gson.Gson;

public class JsonReader implements IFileReader {

	@Override
	public String[] supportTypes() {
		return new String[] {"json"};
	}



	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> convert(LogNode node, Reader reader) throws IOException {
		Gson gson = new Gson();
		Map<String,Object> map = gson.fromJson(reader, Map.class);
		return map;
	}
	


}
