package com.blzeecraft.virtualmenu.core.config.template;

import java.util.Map;
import java.util.function.Function;

import com.blzeecraft.virtualmenu.core.config.map.Maps;
import com.blzeecraft.virtualmenu.core.config.node.ObjectNode;
import com.blzeecraft.virtualmenu.core.config.object.ObjectParser;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.val;

@FunctionalInterface
public interface ITemplate<T> extends Function<LogNode, T> {

	/**
	 * 用模板创建对象
	 * 
	 * @return 创建的对象
	 */
	@Override
	T apply(LogNode node);

	default ITemplate<T> init(LogNode logNode, Map<String, Object> map) {
		try {
			val lowerMap = Maps.lowerCase(map);
			for (val field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				ObjectNode node = field.getAnnotation(ObjectNode.class);
				if (node == null)
					continue;
				Object o = null;
				for (val key : node.key()) {
					o = lowerMap.get(key.toLowerCase());
					if (o != null)
						break;
				}
				Object value = new ObjectParser(o).asObject(field, logNode);
				field.set(this, value);
			}
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		return this;
	}

}
