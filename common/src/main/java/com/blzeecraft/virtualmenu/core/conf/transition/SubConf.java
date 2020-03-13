package com.blzeecraft.virtualmenu.core.conf.transition;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

/**
 * 用于标记 {@link StandardConf} 中子类型
 * @author colors_wind
 *
 */
public class SubConf {
	
	
	@NonNull
	@SneakyThrows
	public Map<String,Object> serialize() {
		val map = new LinkedHashMap<String, Object>();
		for(val field : this.getClass().getFields()) {
			val value = field.get(this);
			val key = field.getName().replace('_', '-');
			if (value == null) {
				map.put(key, null);
			} else if (value instanceof List) {
				map.put(key, unWrapList(value));
			} else if (value instanceof Map) {
				map.put(key, unWrapMap(value));
			} else if (value instanceof Optional) {
				map.put(key, unWrapOptional(value));
			} else {
				map.put(key, unWrapObject(value));
			}
		}
		return map;
	}
	
	public List<?> unWrapList(Object value) {
		return ((List<?>)value).stream().map(this::unWrapObject).collect(Collectors.toList());
	}
	
	public Map<?, ?> unWrapMap(Object value) {
		return ((Map<?, ?>)value).entrySet().stream().collect(Collectors.toMap(k -> k, this::unWrapObject));
	}
	
	public Object unWrapOptional(Object value) {
		return ((Optional<?>)value).map(this::unWrapObject).orElse(null);
	}

	public Object unWrapObject(Object obj) {
		if (obj instanceof OptionalInt) {
			if (((OptionalInt) obj).isPresent()) {
				return ((OptionalInt) obj).getAsInt();
			}
		} else if (obj instanceof OptionalLong) {
			if (((OptionalLong) obj).isPresent()) {
				return ((OptionalLong) obj).getAsLong();
			}
		} else if (obj instanceof OptionalDouble) {
			if (((OptionalDouble) obj).isPresent()) {
				return ((OptionalDouble) obj).getAsDouble();
			}
		} else if (obj instanceof SubConf) {
			return ((SubConf) obj).serialize();
		}
		return obj;
	}

}
