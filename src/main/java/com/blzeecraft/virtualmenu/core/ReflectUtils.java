package com.blzeecraft.virtualmenu.core;

import java.lang.reflect.Field;

import com.blzeecraft.virtualmenu.core.config.deserializer.IDeserializer;
import com.blzeecraft.virtualmenu.core.config.node.DataType;
import com.blzeecraft.virtualmenu.core.config.node.DeserializeBy;
import com.blzeecraft.virtualmenu.core.config.node.EnumKey;
import com.blzeecraft.virtualmenu.core.config.node.ObjectNode;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

public class ReflectUtils {
	
	public static DataType getValueType(Field field) {
		val value = field.getAnnotation(ObjectNode.class);
		return value == null ? DataType.OBJECT : value.type();
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> Class<T> getKeyType(Field field) {
		val key = field.getAnnotation(EnumKey.class);
		return key == null ? null : (Class<T>)key.value();
	}
	
	@SneakyThrows
	public static IDeserializer<?> getDeserializeBy(Field field) {
		var db = field.getAnnotation(DeserializeBy.class);
		if (db == null) {
			db = field.getDeclaringClass().getAnnotation(DeserializeBy.class);
		}
		if (db != null) {
			return db.value().newInstance();
		}
		return null;
	}

}
