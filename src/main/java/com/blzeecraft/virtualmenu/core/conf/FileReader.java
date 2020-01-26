package com.blzeecraft.virtualmenu.core.conf;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

public class FileReader {
	
	public static StandardMenuFile convert(LogNode node, Map<String, Object> map) {
		return convertSection(node, StandardMenuFile.class, map);
	}
	
	/**
	 * 反序列化 {@link SubSection} 
	 * @param <T>
	 * @param node
	 * @param clazz
	 * @param map
	 * @return
	 */
	protected static <T extends SubSection> T convertSection(LogNode node, Class<T> clazz, Map<String, Object> map) {
		try {
			T obj = clazz.newInstance();
			for(Field f : clazz.getFields()) {
				try {
					Class<?> type = f.getType();
					String name = f.getName();
					ObjectWrapper oper = new ObjectWrapper(map.get(name));
					if (type == List.class) {
						ObjectType oType = f.getAnnotation(ObjectType.class);
						if (oType == null) {
							f.set(obj,  oper.asStringList());
						} else {
							LogNode subNode = node.sub(name).list();
							Class<? extends SubSection> subType = oType.value();
							List<? extends SubSection> value = oper.asObjectList().stream().map(objMap -> convertSection(subNode, subType, objMap)).collect(Collectors.toList());
							f.set(obj, value);
						}
					} else if (type == Map.class) {
						ObjectType oType = f.getAnnotation(ObjectType.class);
						if (oType == null) {
							f.set(obj,  oper.asS2StringMap());
						} else {
							LogNode subNode = node.sub(name);
							Class<? extends SubSection> subType = oType.value();
							Map<String, SubSection> value = new LinkedHashMap<>();
							oper.asS2ObjectMap().entrySet().stream().forEach(en -> {
								String subName = en.getKey();
								Map<String, Object> objMap = new ObjectWrapper(en.getValue()).asS2ObjectMap();
								value.put(subName, convertSection(subNode, subType, objMap));
							});
							f.set(obj, value);
						}
					} else if (type == Optional.class) {
						ObjectType oType = f.getAnnotation(ObjectType.class);
						if (oType == null) {
							f.set(obj,  oper.asOptString());
						} else {
							if (oper.isPresent()) {
								LogNode subNode = node.sub(name);
								Class<? extends SubSection> subType = oType.value();
								f.set(obj, convertSection(subNode, subType, oper.asS2ObjectMap()));
							} else {
								f.set(obj,  Optional.empty());
							}
						}
					} else if (SubSection.class.isAssignableFrom(clazz)) {
						LogNode subNode = node.sub(name);
						f.set(obj, convertSection(subNode, clazz, oper.asS2ObjectMap()));
					} else {
						f.set(obj, oper.asObject(node.sub(name), clazz));
					}
				} catch (Exception e) {
					PluginLogger.severe(node, "设置配置子类 " + clazz.getSimpleName() + " 时出错");
					e.printStackTrace();
				}
			}
			return obj;
		} catch (Exception e) {
			PluginLogger.severe(node, "创建配置子类 " + clazz.getSimpleName() + " 时出错.");
			e.printStackTrace();
			return null;
		}
	}

}
