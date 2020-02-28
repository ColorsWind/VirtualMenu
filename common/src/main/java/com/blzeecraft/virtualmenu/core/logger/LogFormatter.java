package com.blzeecraft.virtualmenu.core.logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LogFormatter {
	
	public static String toString(Object o) {
		if (o == null) {
			return "null";
		} else if (o instanceof Integer) {
			return toStringInt((Integer) o);
		} else if (o instanceof Long) {
			return toStringLong((Long) o);
		} else if (o instanceof Object[]) {
			return toStringArray((Object[]) o);
		} else if (o instanceof Map) {
			return toStringMap((Map<?, ?>) o);
		} else if (o instanceof List) {
			return toStringList((List<?>) o);
		} else {
			return o.toString();
		}
	}
	
	public static String toStringObject(Object o) {
		return Objects.toString(o);
	}
	
	public static String toStringInt(Integer i) {
		return i.toString();
	}
	
	public static String toStringLong(Long l) {
		return l.toString();
	}
	
	public static String toStringArray(Object[] array) {
		return Arrays.toString(array);
	}
	
	public static String toStringMap(Map<?,?> map) {
		return map.toString();
	}
	
	public static String toStringList(List<?> list) {
		return list.toString();
	}
	
	

}
