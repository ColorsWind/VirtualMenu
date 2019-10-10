package com.blzeecraft.virtualmenu.core.config.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.config.object.DataType;
import com.blzeecraft.virtualmenu.core.config.object.ObjectParser;

import lombok.val;

public class Maps {

	public static <T> Map<String, T> lowerCase(Map<String, T> originMap) {
		val lowerMap = new LinkedHashMap<String, T>();
		originMap.forEach((k, v) -> lowerMap.put(k.toLowerCase(), v));
		return lowerMap;
	}

	private static final Set<Class<?>> OPTIONAL_CLASS = new HashSet<>(
			Arrays.asList(Optional.class, OptionalLong.class, OptionalInt.class, OptionalDouble.class));

	public static Object convert(Object o, DataType dataType, Class<?> declaringType) {
		// 先处理特殊情况：可选元素未设置
		if (OPTIONAL_CLASS.contains(dataType.getDeclaringClass())) {
			if (o == null) {
				return convertEmpty(dataType);
			} else {
				return convertNullable(new ObjectParser(o), dataType);
			}
		} else {
			return convertNonNull(new ObjectParser(o), dataType);
		}

	}

	private static Object convertNonNull(ObjectParser parser, DataType dataType) {
		switch (dataType) {
		case BOOLEAN:
			return parser.asBoolean();
		case DOUBLE:
			return parser.asDouble();
		case INTEGER:
			return parser.asInteger();
		case LONG:
			return parser.asLong();
		case STRING:
			return parser.asString();
		case STRING_LIST:
			return parser.asStringList();
		default:
			return parser.asRawObject();
		}
	}

	private static Object convertNullable(ObjectParser parser, DataType dataType) {
		switch (dataType) {
		case BOOLEAN:
			return parser.asBoolean();
		case DOUBLE:
			return parser.asOptDouble();
		case INTEGER:
			return parser.asOptInteger();
		case LONG:
			return parser.asOptLong();
		case STRING:
			return Optional.of(parser.asString());
		case STRING_LIST:
			// 不会发生
			return Optional.of(parser.asStringList());
		default:
			return Optional.of(parser.asRawObject());
		}
	}

	public static Object convertEmpty(DataType dataType) {
		switch (dataType) {
		case STRING_LIST:
			return Collections.emptyList();
		case DOUBLE:
			return OptionalDouble.empty();
		case INTEGER:
			return OptionalInt.empty();
		case LONG:
			return OptionalLong.empty();
		default:
			return Optional.empty();
		}

	}

}
