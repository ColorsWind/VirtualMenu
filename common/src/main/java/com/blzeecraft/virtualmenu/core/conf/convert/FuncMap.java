package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import lombok.NonNull;



public class FuncMap implements BiFunction<Map<String, Object>, Class<?>, Map<String, ?>> {

	@Override
	@NonNull
	public Map<String, ?> apply(Map<String, Object> map, @NonNull Class<?> type) {
		if (map == null) {
			return Collections.emptyMap();
		}
		map.replaceAll((k, v) -> ConvertFunctions.convertObject(v, type));
		return map;
	}




}
