package com.blzeecraft.virtualmenu.core.config;

import java.util.Map;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;

/**
 * 代表一个解析的配置
 * @author colors_wind
 *
 */
@ToString
public class ResolvedLineConfig {
	
	private final Map<String, String> values;
	
	public ResolvedLineConfig(@NonNull Map<String, String> values) {
		this.values = values;
	}
	
	public String getAsString(@NonNull String key) throws InvalidLineObjectException {
		val value = values.get(key);
		if (value == null) {
			throw new InvalidLineObjectException("找不到键为 " + key + " 的数据, 已加载的数据: " + values.toString() + ".");
		}
		return value;
	}
	
	public int getAsInt(@NonNull String key) throws InvalidLineObjectException {
		val value = getAsString(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new InvalidLineObjectException("无法将: " + value + " 转化为 int 类型的数据.");
		}
	}
	
	public long getAsLong(@NonNull String key) throws InvalidLineObjectException {
		val value = getAsString(key);
		try {
			return Long.parseLong(value);
		} catch (InvalidLineObjectException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 long 类型的数据.");
		}
	}
	
	public float getAsFloat(@NonNull String key) throws InvalidLineObjectException {
		val value = getAsString(key);
		try {
			return Float.parseFloat(value);
		} catch (InvalidLineObjectException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 float 类型的数据.");
		}
	}
	
	public double getAsDouble(@NonNull String key) throws InvalidLineObjectException {
		val value = getAsString(key);
		try {
			return Double.parseDouble(value);
		} catch (InvalidLineObjectException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 double 类型的数据.");
		}
	}
	
	public boolean getAsBoolean(@NonNull String key) throws InvalidLineObjectException {
		return Boolean.parseBoolean(getAsString(key));
	}

}
