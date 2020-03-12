package com.blzeecraft.virtualmenu.core.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.conf.line.MissingRequiredObjectException;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;
import lombok.var;

/**
 * Object 解析器.
 * @author colors_wind
 *
 */
public final class ObjectWrapper {
	
	public static final Set<Class<?>> SUPPORT_TYPE;
	static {
		HashSet<Class<?>> supports = new HashSet<>();
		supports.addAll(Arrays.asList(int.class, Integer.class, OptionalInt.class)); //int
		supports.addAll(Arrays.asList(long.class, Long.class, OptionalLong.class)); //long
		supports.addAll(Arrays.asList(double.class, Double.class, OptionalDouble.class)); //double
		supports.add(String.class);
		SUPPORT_TYPE = Collections.unmodifiableSet(supports);
	}

	protected final Optional<?> origin;

	public ObjectWrapper(Object origin) {
		this.origin = Optional.ofNullable(origin);
	}

	public Optional<String> asOptString() {
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

	public Object asRawObject() {
		return origin.orElseThrow(() -> new MissingRequiredObjectException("错误, 该项目必须设置."));
	}

	public Optional<?> asRawObjectOpt() {
		return origin;
	}

	public static <T> Optional<T> sneakyThrow(LogNode node, Supplier<T> supplier) {
		try {
			return Optional.of(supplier.get());
		} catch (ObjectConvertException | MissingRequiredObjectException e) {
		}
		return Optional.empty();
	}

	public boolean isPresent() {
		return origin.isPresent();
	}

	public Object asObject(LogNode node, Class<?> declaringType) {
		if (OptionalInt.class == declaringType) {
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

	public Map<String, Object> asS2ObjectMap() {
		if (origin.isPresent()) {
			val o = origin.get();
			if (o instanceof Map) {
				return ((Map<?, ?>) o).entrySet().stream()
						.collect(Collectors.toMap(en -> en.getKey().toString(), Entry::getValue));
			}
		}
		return new LinkedHashMap<>();
	}

	public Map<String, String> asS2StringMap() {
		Map<String, String> sMap = new LinkedHashMap<>();
		asS2ObjectMap().entrySet().forEach(en -> sMap.put(en.getKey(), new ObjectWrapper(en.getValue()).asString()));
		return sMap;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> asObjectList() {
		return origin.isPresent() ? (List<Map<String, Object>>) origin.get() : Collections.emptyList();
	}
	
	public ObjectWrapper getValue(String key) {
		return new ObjectWrapper(asS2ObjectMap().get(key));
	}

}
