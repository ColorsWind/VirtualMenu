package com.blzeecraft.virtualmenu.core.config.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import com.blzeecraft.virtualmenu.core.config.MissingRequiredObjectException;
import com.blzeecraft.virtualmenu.core.config.ObjectConvertException;

import lombok.val;
import lombok.var;

public class ObjectParser {

	protected final Object origin;

	public ObjectParser(Object origin) {
		if (origin == null) {
			throw new MissingRequiredObjectException("错误, 该项目必须设置.");
		}
		this.origin = origin;
	}

	public String asString() {
		return origin.toString();
	}
	
	public List<String> asStringList() {
		if (origin instanceof List) {
			List<?> list = (List<?>) origin;
			if (list.isEmpty()) {
				return Collections.emptyList();
			} else if (String.class == list.iterator().next().getClass()){
				@SuppressWarnings("unchecked")
				List<String> stringList = (List<String>) origin;
				return stringList;
			} else {
				return Collections.emptyList();
			}
		}
		// 以";"分隔, 支持转义"\;"
		val list = new ArrayList<String>();
		val chars = asString().toCharArray();
		var cache = new StringBuilder();
		var escape = false;
		// 循环处理所有字符
		for (char c : chars) {
			if (escape) {
				escape = false;
				cache.append(c);
			} else if ('\\' == c) {
				escape = true;
			} else if (';' == c) {
				list.add(cache.toString());
				cache = new StringBuilder();
			} else {
				cache.append(c);
			}
		}
		// 处理最后一项
		if (cache.length() != 0) {
			list.add(cache.toString());
		}
		return list;
	}

	
	public OptionalInt asOptInteger() {
		if (origin instanceof Number) {
			return OptionalInt.of(((Number) origin).intValue());
		}
		try {
			return OptionalInt.of(Integer.parseInt(asString()));
		} catch (NumberFormatException e) {}
		return OptionalInt.empty();
	}

	public int asInteger() throws ObjectConvertException {
		return asOptInteger().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Integer"));
	}
	
	public OptionalLong asOptLong() {
		if (origin instanceof Number) {
			return OptionalLong.of(((Number) origin).longValue());
		}
		try {
			return OptionalLong.of(Long.parseLong(asString()));
		} catch (NumberFormatException e) {}
		return OptionalLong.empty();
	}

	public long asLong() throws ObjectConvertException {
		return asOptLong().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Long"));
	}
	
	public OptionalDouble asOptDouble() {
		if (origin instanceof Number) {
			return OptionalDouble.of(((Number) origin).doubleValue());
		}
		try {
			return OptionalDouble.of(Long.parseLong(asString()));
		} catch (NumberFormatException e) {}
		return OptionalDouble.empty();
	}

	public double asDouble() throws ObjectConvertException {
		return asOptDouble().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Double"));
	}

	public boolean asBoolean() {
		return origin instanceof Boolean ? ((Boolean) origin).booleanValue() : Boolean.parseBoolean(asString());
	}
	
	public Object asRawObject() {
		return origin;
	}

}
