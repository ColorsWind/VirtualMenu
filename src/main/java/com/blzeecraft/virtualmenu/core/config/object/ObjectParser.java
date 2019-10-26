package com.blzeecraft.virtualmenu.core.config.object;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.blzeecraft.virtualmenu.core.ReflectUtils;
import com.blzeecraft.virtualmenu.core.config.MissingRequiredObjectException;
import com.blzeecraft.virtualmenu.core.config.ObjectConvertException;
import com.blzeecraft.virtualmenu.core.config.node.DataType;
import com.blzeecraft.virtualmenu.core.config.node.ObjectNode;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;
import lombok.var;

public final class ObjectParser {

	protected final Optional<?> origin;

	public ObjectParser(Object origin) {
		this.origin = Optional.ofNullable(origin);
	}

	public Optional<String> asStringOpt() {
		return origin.map(s -> s.toString());
	}

	public String asString() {
		return asRawObject().toString();
	}

	public List<String> asStringList() {
		return origin.map(object -> {
			if (object instanceof List) {
				List<?> list = (List<?>) object;
				if (list.isEmpty()) {
					List<String> empty = Collections.emptyList();
					return empty;
				} else if (String.class == list.iterator().next().getClass()) {
					@SuppressWarnings("unchecked")
					List<String> stringList = (List<String>) object;
					return stringList;
				} else {
					List<String> empty = Collections.emptyList();
					return empty;
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
		}).orElse(Collections.emptyList());
	}
	
	public Map<String, Object> asStringMap() {
		if (origin.isPresent()) {
			val o = origin.get();
			if (o instanceof Map) {
				return ((Map<?,?>)o).entrySet().stream().collect(Collectors.toMap(en -> en.getKey().toString(), Entry::getValue));
			}
		}
		return new LinkedHashMap<>();
	}

	public OptionalInt asOptInteger() {
		if (origin.isPresent()) {
			val object = origin.get();
			if (object instanceof Number) {
				return OptionalInt.of(((Number) object).intValue());
			}
			try {
				return OptionalInt.of(Integer.parseInt(asString()));
			} catch (NumberFormatException e) {
			}
		}
		return OptionalInt.empty();
	}

	public int asInteger() throws ObjectConvertException {
		return asOptInteger().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Integer"));
	}

	public OptionalLong asOptLong() {
		if (origin.isPresent()) {
			val object = origin.get();
			if (object instanceof Number) {
				return OptionalLong.of(((Number) object).longValue());
			}
			try {
				return OptionalLong.of(Long.parseLong(asString()));
			} catch (NumberFormatException e) {
			}
		}

		return OptionalLong.empty();
	}

	public long asLong() throws ObjectConvertException {
		return asOptLong().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Long"));
	}

	public OptionalDouble asOptDouble() {
		if (origin.isPresent()) {
			val object = origin.get();
			if (object instanceof Number) {
				return OptionalDouble.of(((Number) object).doubleValue());
			}
			try {
				return OptionalDouble.of(Long.parseLong(asString()));
			} catch (NumberFormatException e) {
			}
		}
		return OptionalDouble.empty();
	}

	public double asDouble() throws ObjectConvertException {
		return asOptDouble().orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 Double"));
	}

	public boolean asBoolean() {
		if (origin.isPresent()) {
			val object = origin.get();
			return object instanceof Boolean ? ((Boolean) object).booleanValue() : Boolean.parseBoolean(asString());
		}
		return false;
	}
	
	public <T extends Enum<T>> Optional<T> asEnumOpt(Class<T> clazz) {
		try {
			return Optional.of(Enum.valueOf(clazz, asString()));
		} catch (IllegalArgumentException e) {}
		return Optional.empty();
	}
	
	public <T extends Enum<T>> T asEnum(Class<T> clazz) {
		return asEnumOpt(clazz).orElseThrow(() -> new ObjectConvertException("无法将: " + asString() + " 转换成 " + clazz.getTypeName() + ". 可用的枚举类型: " + Arrays.toString(clazz.getEnumConstants())));
	}

	public Object asRawObject() {
		return origin.orElseThrow(() -> new MissingRequiredObjectException("错误, 该项目必须设置."));
	}

	public Optional<?> asRawObjectOpt() {
		return origin;
	}
	
	public static <T> Optional<T> sneakyThrow(LogNode node, Supplier<T> supplier) {
		try {
			return Optional.of(supplier.get());
		} catch (ObjectConvertException | MissingRequiredObjectException e) {}
		return Optional.empty();
	}

	public boolean isPresent() {
		return origin.isPresent();
	}

	public Optional<?> asObjectOpt(DataType type) {
		switch (type) {
		case BOOLEAN:
			return isPresent() ? Optional.of(asBoolean()) : Optional.empty();
		case DOUBLE:
			val optDouble = asOptDouble();
			return optDouble.isPresent() ? Optional.of(optDouble.getAsDouble()) : Optional.empty();
		case INTEGER:
			val optInteger = asOptInteger();
			return optInteger.isPresent() ? Optional.of(optInteger.getAsInt()) : Optional.empty();
		case LONG:
			val optLong = asOptLong();
			return optLong.isPresent() ? Optional.of(optLong.getAsLong()) : Optional.empty();
		case STRING:
			return asStringOpt();
		default:
			return origin;
		}
	}
	
	public Object asObject(DataType type) {
		switch (type) {
		case BOOLEAN:
			return asBoolean();
		case DOUBLE:
			return asDouble();
		case INTEGER:
			return asInteger();
		case LONG:
			return asLong();
		case STRING:
			return asString();
		default:
			return origin;
		}
	}

	
	public Object asObject(Field field, LogNode node) {
		val deserializeBy = ReflectUtils.getDeserializeBy(field);
		if (deserializeBy != null) {
			// 如果已经指定了反序列化器，直接使用
			return deserializeBy.init(node, asStringMap()).apply(node);
		}
		val declaringType = field.getDeclaringClass();
		if (Optional.class == declaringType) {
			val value = field.getAnnotation(ObjectNode.class);
			return asObjectOpt(value == null ? DataType.STRING : value.type());
		} else if (List.class == declaringType) {
			val type = ReflectUtils.getValueType(field);
			if (type == DataType.STRING) {
				return asStringList();
			}
			return asStringList().stream().map(
					s -> ObjectParser.sneakyThrow(node, () -> asObject(type)).map(Stream::of).orElseGet(Stream::empty))
					.collect(Collectors.toCollection(ArrayList::new));
		} else if (Map.class == declaringType) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Class<? extends Enum> keyType = ReflectUtils.getKeyType(field);
			DataType valueType = ReflectUtils.getValueType(field);
			if (keyType == null) {
				return asStringMap();
			} else {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Map<Object, Object> enumMap = new EnumMap(keyType);
				asStringMap().forEach((k,v) -> {
					@SuppressWarnings({ "unchecked", "rawtypes" })
					val key = ObjectParser.sneakyThrow(node, () -> new ObjectParser(k).asEnum((Class<Enum>)keyType));
					val value = ObjectParser.sneakyThrow(node,  () -> new ObjectParser(v).asObject(valueType));
					if (key.isPresent() && value.isPresent()) {
						enumMap.put(key, value);
					}
				});
				return enumMap;
			}
		} else if (OptionalInt.class == declaringType) {
			return asOptInteger();
		} else if (OptionalLong.class == declaringType) {
			return asOptLong();
		} else if (OptionalDouble.class == declaringType) {
			return asOptDouble();
		} else if (int.class == declaringType || Integer.class == declaringType) {
			return asInteger();
		} else if (long.class == declaringType || Long.class == declaringType) {
			return asLong();
		} else if (double.class == declaringType || Double.class == declaringType) {
			return asDouble();
		} else if (boolean.class == declaringType || Boolean.class == declaringType) {
			return asBoolean();
		}
		return asRawObject();
	}

}
