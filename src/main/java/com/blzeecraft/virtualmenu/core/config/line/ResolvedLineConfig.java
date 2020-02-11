package com.blzeecraft.virtualmenu.core.config.line;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.StringTokenizer;

import com.blzeecraft.virtualmenu.core.config.MissingRequiredObjectException;
import com.blzeecraft.virtualmenu.core.config.ObjectConvertException;
import com.blzeecraft.virtualmenu.core.config.object.ObjectParser;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;

/**
 * 代表一个已解析的单行配置
 * 
 * @author colors_wind
 *
 */
@ToString
public class ResolvedLineConfig {

	// 我们约定所有key为小写
	private final Map<String, String> values;

	public ResolvedLineConfig(@NonNull Map<String, String> values) {
		this.values = new HashMap<>();
		values.forEach((k, v) -> this.values.put(k.toLowerCase(), v));
	}


	public boolean isSet(@NonNull String key) {
		return values.containsKey(key);
	}
	
	public Optional<String> getAsOptString(@NonNull String key) {
		return Optional.ofNullable(values.get(key));
	}

	public String getAsString(@NonNull String key) throws MissingRequiredObjectException {
		val value = values.get(key.toLowerCase());
		if (value == null) {
			throw new MissingRequiredObjectException("找不到键为 " + key + " 的数据, 已加载的数据: " + values.toString() + ".");
		}
		return value;
	}
	
	public OptionalInt getAsOptInt(@NonNull String key) {
		return new ObjectParser(getAsString(key)).asOptInteger();
	}

	public int getAsInt(@NonNull String key) throws ObjectConvertException {
		return new ObjectParser(getAsString(key)).asInteger();
	}
	
	public OptionalLong getAsOptLong(@NonNull String key) {
		return new ObjectParser(getAsString(key)).asOptLong();
	}

	public long getAsLong(@NonNull String key) throws ObjectConvertException {
		return new ObjectParser(getAsString(key)).asLong();
	}
	
	public OptionalDouble getAsOptDouble(@NonNull String key) {
		return new ObjectParser(getAsString(key)).asOptDouble();
	}

	public double getAsDouble(@NonNull String key) throws ObjectConvertException {
		return new ObjectParser(getAsString(key)).asDouble();
	}

	public boolean getAsBoolean(@NonNull String key) {
		return Boolean.parseBoolean(getAsString(key));
	}

	public Optional<Boolean> getAsOptBoolean(@NonNull String key) {
		return getAsOptString(key).map(s -> Boolean.valueOf(s));
	}

	public <T extends Enum<T>> Optional<Set<T>> getAsOptEnumSet(@NonNull String key, @NonNull Class<T> clazz) {
		return this.getAsOptString(key).map(s -> {
			val str = new StringTokenizer(s, "-");
			val set = new HashSet<T>(); // 临时储存
			while (str.hasMoreTokens()) {
				set.add(Enum.valueOf(clazz, str.nextToken().toUpperCase()));
			}
			return EnumSet.copyOf(set);
		});
	}

}