package com.blzeecraft.virtualmenu.core.conf.convert;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;
import com.blzeecraft.virtualmenu.core.conf.transition.SubConf;
import com.google.common.base.Optional;

import lombok.NonNull;
import lombok.val;

public class ConvertFunctions {
	public static final FuncString TO_STRING = new FuncString();
	public static final FuncInteger TO_INTEGER = new FuncInteger();
	public static final FuncLong TO_LONG = new FuncLong();
	public static final FuncDouble TO_DOUBLE = new FuncDouble();
	public static final FuncOptionalInt TO_OPTIONAL_INT = new FuncOptionalInt();
	public static final FuncOptionalLong TO_OPTIONAL_LONG = new FuncOptionalLong();
	public static final FuncOptionalDouble TO_OPTIONAL_DOUBLE = new FuncOptionalDouble();
	public static final FuncSubConf TO_SUBCONF = new FuncSubConf();
	public static final FuncS2ObjectMap TO_S2OBJECT_MAP = new FuncS2ObjectMap();
	public static final FuncS2StringMap TO_S2STRING_MAP = new FuncS2StringMap();
	public static final FuncOptional TO_OPTIONAL = new FuncOptional();
	public static final FuncList TO_LIST = new FuncList();
	public static final FuncMap TO_MAP = new FuncMap();
	
	@SuppressWarnings("unchecked")
	public static Object convertObject(@NonNull Object obj, @NonNull Class<?> type) {
		if (SubConf.class.isAssignableFrom(type)) {
			return TO_SUBCONF.apply((Class<? extends SubConf>) type, TO_S2OBJECT_MAP.apply(obj));
		} if (String.class == type) {
			return TO_STRING.apply(obj);
		} else if (Integer.class == type || int.class == type) {
			return TO_INTEGER.apply(obj);
		} else if (Long.class == type || long.class == type) {
			return TO_LONG.apply(obj);
		} else if (Double.class == type || double.class == type) {
			return TO_DOUBLE.apply(obj);
		} else if (OptionalInt.class == type) {
			return TO_OPTIONAL_INT.apply(obj);
		} else if (OptionalLong.class == type) {
			return TO_OPTIONAL_LONG.apply(obj);
		} else if (OptionalDouble.class == type) {
			return TO_OPTIONAL_DOUBLE.apply(obj);
		} 
			throw new ObjectConvertException("不支持的字段类型: " + type.getTypeName() + " (#1)");
		
	}
	
	public static Object convertObject(@NonNull Object obj, @NonNull Class<?> type, @NonNull Class<?> genericType) {
		try {
			return convertObject(obj, type);
		} catch (ObjectConvertException e) {
		}
		if (Optional.class == type) {
			return TO_OPTIONAL.apply(obj, genericType);
		} else if (List.class == type) {
			return TO_LIST.apply(obj, genericType);
		} else if (Map.class == type) {
			return TO_MAP.apply(TO_S2OBJECT_MAP.apply(obj), genericType);
		}
		throw new ObjectConvertException("不支持的字段类型: " + type.getTypeName() + " (#2不是基本类型)");
	}
	
	public static Class<?> getGerericType(Field field, int index) {
		val generic = (ParameterizedType)field.getGenericType();
		val actually = (Class<?>)generic.getActualTypeArguments()[index];
		return actually;
	}
	
	
}
