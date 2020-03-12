package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FuncS2ObjectMap implements Function<Object, Map<String, Object>> {

	@Override
	public Map<String, Object> apply(Object obj) {
		if (obj != null && obj instanceof Map) {
			return ((Map<?, ?>) obj).entrySet().stream()
					.collect(Collectors.toMap(en -> en.getKey().toString(), Entry::getValue));
		}
		return new LinkedHashMap<>();
	}

}
