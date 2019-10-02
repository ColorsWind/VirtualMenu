package com.blzeecraft.virtualmenu.core.config;

import java.util.Map;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;

@ToString
public class LineConfig {
	
	private final Map<String, String> values;
	
	public LineConfig(Map<String, String> values) {
		this.values = values;
	}
	
	public String getAsString(@NonNull String key) throws NullPointerException {
		val value = values.get(key);
		if (value == null) {
			throw new NullPointerException("找不到键为 " + key + " 的数据, 已加载的数据: " + values.toString() + ".");
		}
		return value;
	}
	
	public int getAsInt(@NonNull String key) throws NullPointerException {
		val value = getAsString(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 int 类型的数据.");
		}
	}
	
	public long getAsLong(@NonNull String key) throws NullPointerException {
		val value = getAsString(key);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 long 类型的数据.");
		}
	}
	
	public float getAsFloat(@NonNull String key) throws NullPointerException {
		val value = getAsString(key);
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 float 类型的数据.");
		}
	}
	
	public double getAsDouble(@NonNull String key) throws NullPointerException {
		val value = getAsString(key);
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("无法将: " + value + " 转化为 double 类型的数据.");
		}
	}
	
	public boolean getAsBoolean(@NonNull String key) {
		return Boolean.parseBoolean(getAsString(key));
	}

}
