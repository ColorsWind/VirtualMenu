package com.blzeecraft.virtualmenu.core.config.template;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.event.inventory.ClickType;

import com.blzeecraft.virtualmenu.core.config.map.Maps;
import com.blzeecraft.virtualmenu.core.config.object.ObjectNode;

import lombok.val;

public class TemplateSupport {
	
	public void init(ITemplate<?> template, Map<String, Object> map) {
		try {
			val lowerMap = lowerCase(map);
			for (val field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				ObjectNode node = field.getAnnotation(ObjectNode.class);
				if (node == null)
					continue;
				Object o = null;
				// 直接处理Enum
				if (node.enumKey().length > 0) {
					for(val en : (Enum<?>[])node.enumKey()[0].getMethod("values", new Class<?>[0]).invoke(null, new Object[0])) {
						
					}
					continue;
				}
				for (val key : node.key()) {
					o = lowerMap.get(key.toLowerCase());
					if (o != null)
						break;
				}
				Object value = Maps.convert(o, node.type(), field.getDeclaringClass());
				field.set(this, value);
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public void processEnumKey(Map<String, Object> map,  Enum<?>[] enumKey) {
		
	}

	public static <T> Map<String, T> lowerCase(Map<String, T> originMap) {
		val lowerMap = new LinkedHashMap<String, T>();
		originMap.forEach((k, v) -> lowerMap.put(k.toLowerCase(), v));
		return lowerMap;
	}
}
