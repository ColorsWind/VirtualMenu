package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class FuncS2StringMap implements Function<Object, Map<String, String>> {

	@Override
	public Map<String, String> apply(Object obj) {
		if (obj == null && obj instanceof Map) {
			return ((Map<?, ?>) obj).entrySet().stream()
					.collect(Collectors.toMap(en -> en.getKey().toString(), en -> en.getValue().toString()));
		}
		return new LinkedHashMap<>();
	}

}