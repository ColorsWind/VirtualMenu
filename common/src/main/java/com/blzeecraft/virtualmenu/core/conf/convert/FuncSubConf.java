package com.blzeecraft.virtualmenu.core.conf.convert;

import java.awt.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;
import com.blzeecraft.virtualmenu.core.conf.transition.SubConf;

import lombok.NonNull;
import lombok.val;

public class FuncSubConf implements BiFunction<Class<? extends SubConf>, Map<String, Object>, SubConf> {

	@Override
	@NonNull
	public SubConf apply(@NonNull Class<? extends SubConf> clazz, @NonNull Map<String, Object> map) {
		try {
			val instance = clazz.newInstance();
			for(val field : clazz.getFields()) {
				val fieldType = field.getType();
				val fieldName = toFieldName(field.getName());
				val dataValue = map.get(fieldName);
				if (fieldType == Optional.class || fieldType == List.class) {
					val actually = ConvertFunctions.getGerericType(field, 0);
				    val obj = ConvertFunctions.convertObject(dataValue, fieldType, actually);
				    field.set(instance, obj);
				} else if (fieldType == Map.class){
					val actually = ConvertFunctions.getGerericType(field, 1);
				    val obj = ConvertFunctions.convertObject(dataValue, fieldType, actually);
				    field.set(instance, obj);
				} else {
					val obj = ConvertFunctions.convertObject(dataValue, fieldType);
				    field.set(instance, obj);
				}
			}
			return instance;
		} catch (Exception e) {
			throw new ObjectConvertException(e);
		}
	}
	
	public String toFieldName(String key) {
		return key.replace('-', '_');
	}



}
