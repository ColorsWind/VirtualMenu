package com.blzeecraft.virtualmenu.core.conf.standardize;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.conf.ObjectWrapper;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

public class MapToConfFactory {

	public static StandardConf convert(LogNode node, Map<String, Object> map) {
		return mapToSubConf(node, StandardConf.class, map);
	}
	

	/**
	 * 反序列化 {@link SubConf}
	 * @param <T> 目标类型
	 * @param node 当前节点日志标记
	 * @param clazz 目标类型的class
	 * @param map 待解析的类型
	 * @return 目标实例
	 */
	protected static <T extends SubConf> T mapToSubConf(LogNode node, Class<T> clazz, Map<String, Object> map) {
		try {
			T obj = clazz.newInstance();
			for (Field f : clazz.getFields()) {
				try {
					Class<?> type = f.getType(); 
					String name = f.getName();
					LogNode subNode = node.sub(name);
					ObjectWrapper oper = new ObjectWrapper(map.get(name));
					if (ObjectWrapper.SUPPORT_TYPE.contains(type)) { // 简单类型
						f.set(obj, oper.asObject(subNode, type));
					} else if (SubConf.class.isAssignableFrom(type)) { //SubConf类型
						@SuppressWarnings("unchecked")
						SubConf value = mapToSubConf(subNode, (Class<? extends SubConf>)type, oper.asS2ObjectMap());
						f.set(obj, value);
					} else { // 复杂类型 Optional<E> List<E> 和 Map<String,E>
						ObjectType oType = f.getAnnotation(ObjectType.class);
						Class<?> genericType = oType == null ? String.class : oType.value();
						Object value = objectToObject(subNode, type, oper, genericType);
						f.set(obj, value);
					}
				} catch (Exception e) {
					PluginLogger.severe(node, "读取配置文件时(#processField-" +  clazz.getSimpleName() + "." + f.getName() +")出错.");
					e.printStackTrace();
				}
			}
			return obj;
		} catch (Exception e) {
			PluginLogger.severe(node, "读取配置文件时(#newInstance-" +  clazz.getSimpleName() + ")出错.(请联系插件作者)");
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * 反序列化复杂类型
	 * @param <E> 目标类型
	 * @param subNode 当前节点日志标记
	 * @param originType 复杂类型的class
	 * @param oper 被包装的复杂类型
	 * @param genericType 复杂类型的泛型类型
	 * @return 目标实例
	 */
	@SuppressWarnings("unchecked")
	protected static <E> E objectToObject(LogNode subNode, Class<E> originType, ObjectWrapper oper, Class<?> genericType) {
		//先对泛型类型进行合法性检验
		if (String.class == genericType) {
			if (originType == List.class) {
				return (E) oper.asStringList();
			} else if (originType == Map.class) {
				return (E) oper.asS2StringMap();
			} else if (originType == Optional.class) {
				return (E) oper.asOptString();
			}
		} else if (SubConf.class.isAssignableFrom(genericType)) {
			Class<? extends SubConf> subSectionType = (Class<? extends SubConf>) genericType;
			if (List.class == originType) {
				List<? extends SubConf> value = oper.asObjectList().stream()
						.map(objMap -> mapToSubConf(subNode.list() /*list标记*/, subSectionType, objMap))
						.collect(Collectors.toList());
				return (E) value;
			} else if (Map.class == originType) {
				Map<String, SubConf> value = new LinkedHashMap<>();
				oper.asS2ObjectMap().entrySet().stream().forEach(en -> {
					String subName = en.getKey();
					Map<String, Object> objMap = new ObjectWrapper(en.getValue()).asS2ObjectMap();
					value.put(subName, mapToSubConf(subNode, subSectionType, objMap));
				});
				return (E) value;
			} else if (Optional.class == originType) {
				if (oper.isPresent()) {
					return (E) mapToSubConf(subNode, subSectionType, oper.asS2ObjectMap());
				} else {
					return (E) Optional.empty();
				}
			}
		} 
		throw new IllegalFieldException(subNode, originType);
	}

}
