package com.blzeecraft.virtualmenu.core.conf.convert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;
import com.blzeecraft.virtualmenu.core.conf.transition.SubConf;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import lombok.NonNull;
import lombok.val;

public class FuncSubConf implements BiFunction<Class<? extends SubConf>, Map<String, Object>, SubConf> {
	public static final LogNode LOG_NODE = LogNode.of("MapToSubConf");

	@Override
	@NonNull
	public SubConf apply(@NonNull Class<? extends SubConf> clazz, @NonNull Map<String, Object> map) {
		val instance = initalize(clazz).orElseThrow(ObjectConvertException::new);
		for(val field : clazz.getFields()) {
			val fieldName = toFieldName(field.getName());
			val dataValue = map.get(fieldName);
			processField(instance, field, dataValue);
		}
		return instance;
	}
	
	public void processField(SubConf instance, Field field, Object dataValue) {
		try {
			val fieldType = field.getType();
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
		} catch (Exception e) {
			PluginLogger.severe(LOG_NODE, "处理字段 " + field.getDeclaringClass().getSimpleName() + "." + field.getName() + " 时出现异常.");
			throw new ObjectConvertException(e);
		}
	}
	
	private Optional<SubConf> initalize(@NonNull Class<? extends SubConf> clazz) {
		try {
			return Optional.of(clazz.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	

	public String toFieldName(String key) {
		return key.replace('-', '_');
	}

}
