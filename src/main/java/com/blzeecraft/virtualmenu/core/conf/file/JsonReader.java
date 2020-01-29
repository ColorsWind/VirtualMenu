package com.blzeecraft.virtualmenu.core.conf.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.google.gson.Gson;

public class JsonReader implements IFileReader {

	@Override
	public String[] supportTypes() {
		return new String[] {"json"};
	}

	@Override
	public Map<String, Object> convert(File file, LogNode node) throws IOException {
		Gson gson = new Gson();
		InputStream ins = new FileInputStream(file);
		Reader reader = new InputStreamReader(ins, IFileReader.ENCODE);
		@SuppressWarnings("unchecked")
		Map<String,Object> map = gson.fromJson(reader, Map.class);
		return map;
	}

}
