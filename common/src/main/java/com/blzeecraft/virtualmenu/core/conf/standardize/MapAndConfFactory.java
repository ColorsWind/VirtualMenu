package com.blzeecraft.virtualmenu.core.conf.standardize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.conf.ObjectWrapper;
import com.blzeecraft.virtualmenu.core.conf.OptionalMapper;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import lombok.val;

/**
 * 有关 Map 和 Conf 相互转换的工厂方法.
 * <p>
 * 名词解释:
 * <p>
 * 简单类型 = int, long, double, String, 以及它们的Optional包装类型. 对应相应的类型.
 * <p>
 * 容器类型 = Optional<E> List<E> 和 Map<String,E>. 对应相应的Object List 和 Map.
 * <p>
 * 子表类型 = SubConf 的子类, 可内嵌这里介绍的三种类型. 对应Map<String, Object>.
 * 
 * @author colors_wind
 *
 */
public class MapAndConfFactory {
	// 可直接储存在文件中的类型.
	public static final Set<Class<?>> DIRECT_TYPE;
	static {
		val type = new HashSet<Class<?>>();
		type.addAll(Arrays.asList(int.class, Integer.class));
		type.addAll(Arrays.asList(int.class, Integer.class));
		type.addAll(Arrays.asList(double.class, Double.class));
		type.add(String.class);
		DIRECT_TYPE = Collections.unmodifiableSet(type);
	}
	// Optional 类型
	public static final Set<Class<?>> OPTIONAL_TYPE;
	static {
		val type = new HashSet<Class<?>>();
		type.add(Optional.class);
		type.add(OptionalInt.class);
		type.add(OptionalLong.class);
		type.add(OptionalDouble.class);
		OPTIONAL_TYPE = Collections.unmodifiableSet(type);
	}
	// 容器类型
	public static final Set<Class<?>> CONTAINER_TYPE;
	static {
		val type = new HashSet<Class<?>>();
		type.add(Optional.class);
		type.add(List.class);
		type.add(Map.class);
		CONTAINER_TYPE = Collections.unmodifiableSet(type);
	}
	// Field 允许的类型
	public static final Set<Class<?>> SUPPORT_TYPE;
	static {
		val supports = new HashSet<Class<?>>();
		supports.addAll(Arrays.asList(Optional.class, List.class, Map.class)); // container
		supports.addAll(Arrays.asList(int.class, Integer.class, OptionalInt.class)); // int
		supports.addAll(Arrays.asList(long.class, Long.class, OptionalLong.class)); // long
		supports.addAll(Arrays.asList(double.class, Double.class, OptionalDouble.class)); // double
		supports.add(String.class); // string
		SUPPORT_TYPE = Collections.unmodifiableSet(supports);
	}

	public static void checkField(Field field) {
		Class<?> type = field.getClass();
		if (!SUPPORT_TYPE.contains(type)) {
			throw new IllegalFieldException(field);
		}
		if (CONTAINER_TYPE.contains(type)) {
			Class<?> genericType = getGenericType(field);
			if (genericType != String.class && !SubConf.class.isAssignableFrom(genericType)) {
				throw new IllegalFieldException(field);
			}
		}
	}

	public static String fieldNameToKey(Field field) {
		return field.getName().replace('_', '-');
	}

	public static Class<?> getGenericType(Field field) {
		ObjectType oType = field.getAnnotation(ObjectType.class);
		return oType == null ? String.class : oType.value();
	}

	/**
	 * 将 SubConf 序列化, 得到 Map.
	 * 
	 * @param node
	 * @param conf
	 * @return
	 */
	public static Map<String, Object> write(LogNode node, SubConf conf) {
		Map<String, Object> valueMap = new LinkedHashMap<>();
		for (Field field : conf.getClass().getFields()) {
			checkField(field);
			Class<?> type = field.getType();
			String name = fieldNameToKey(field);
			LogNode subNode = node.sub(name);
			try {
				Object value = field.get(conf);
				if (OptionalMapper.SUPPORT_TYPE.contains(type)) { // 简单类型
					valueMap.put(name, new OptionalMapper(value).apply());
				} else if (SubConf.class.isAssignableFrom(type)) { // 子表类型
					valueMap.put(name, write(subNode, (SubConf) value));
				} else if (CONTAINER_TYPE.contains(type)){ // 容器类型
					Class<?> genericType = getGenericType(field);
					if (genericType == String.class) {
						if (type == Optional.class) {
							valueMap.put(name, ((Optional<?>) value).orElse(null));
						} else {
							valueMap.put(name, value);
						}
					} else if (SubConf.class.isAssignableFrom(genericType)) { // 子表类型
						if (type == List.class) {
							LogNode listNode = subNode.list();
							List<?> list = ((List<?>) value).stream().map(element -> write(listNode, (SubConf) element))
									.collect(Collectors.toCollection(ArrayList::new));
							valueMap.put(name, list);
						} else if (type == Map.class) {
							Map<?, Object> map = new LinkedHashMap<>((Map<?, ?>) value);
							map.replaceAll((k, v) -> write(subNode.sub(k.toString()), (SubConf) v));
							valueMap.put(name, value);
						} else if (type == Optional.class) {
							Map<?, ?> map = ((Optional<?>) value).map(element -> write(subNode, (SubConf) element))
									.orElse(null);
							valueMap.put(name, map);
						}
					}
				}
			} catch (ClassCastException e) {
				PluginLogger.severe(node, "写入配置文件时(#processField-" + conf.getClass().getSimpleName() + "."
						+ field.getName() + ")出现类型转换异常, 请检查配置文件格式.");
				e.printStackTrace();
			} catch (Exception e) {
				PluginLogger.severe(node,
						"读取配置文件时(#processField-" + conf.getClass().getSimpleName() + "." + field.getName() + ")出现异常.");
				e.printStackTrace();
			}
		}
		return valueMap;
	}

	/**
	 * 反序列化 Map, 得到标准配置.
	 * 
	 * @param node
	 * @param map
	 * @return
	 */
	public static StandardConf read(LogNode node, Map<String, Object> map) {
		return read(node, StandardConf.class, map);
	}

	/**
	 * 反序列化 Map.
	 * 
	 * @param <T>
	 * @param node
	 * @param clazz
	 * @param map
	 * @return
	 */
	public static <T extends SubConf> T read(LogNode node, Class<T> clazz, Map<String, Object> map) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			PluginLogger.severe(node, "读取配置文件时(#newInstance-" + clazz.getSimpleName() + ")出错.(请联系插件作者)");
			e.printStackTrace();
			return null;
		}
		return read(node, instance, map);
	}

	/**
	 * 反序列化 Map.
	 * 
	 * @param <T>
	 * @param node
	 * @param instance
	 * @param dataMap
	 * @return
	 */
	public static <T extends SubConf> T read(LogNode node, T instance, Map<String, Object> dataMap) {
		for (Field field : instance.getClass().getFields()) {
			try {
				checkField(field);
				Class<?> type = field.getType();
				String name = fieldNameToKey(field);
				LogNode subNode = node.sub(name);
				Object data = dataMap.get(name);
				ObjectWrapper objectWrapper = new ObjectWrapper(data);
				if (ObjectWrapper.SUPPORT_TYPE.contains(type)) { // 简单类型
					field.set(instance, objectWrapper.asObject(subNode, type));
				} else if (SubConf.class.isAssignableFrom(type)) { // 子表类型
					@SuppressWarnings("unchecked")
					SubConf conf = read(subNode, (Class<? extends SubConf>) type, objectWrapper.asS2ObjectMap());
					field.set(instance, conf);
				} else if (CONTAINER_TYPE.contains(type)){ // 容器类型
					Class<?> genericType = getGenericType(field);
					if (Optional.class == type) {
						Optional<?> opt = Optional.ofNullable(remapContainerElement(subNode, data, genericType));
						field.set(instance, opt);
					} else if (List.class == type) {
						LogNode listNode = subNode.list();
						List<?> list = ((List<?>) data).stream()
								.map(element -> remapContainerElement(listNode, element, type))
								.collect(Collectors.toCollection(ArrayList::new));
						field.set(instance, list);
					} else if (Map.class == type) {
						Map<?, Object> map = new LinkedHashMap<>(dataMap);
						map.replaceAll((k, v) -> remapContainerElement(subNode.sub(k.toString()), data, type));
						field.set(instance, dataMap);
					}
					field.set(instance, data);
				}
			} catch (ClassCastException e) {
				PluginLogger.severe(node, "读取配置文件时(#processField-" + instance.getClass().getSimpleName() + "."
						+ field.getName() + ")出现类型转换异常, 请检查配置文件格式.");
				e.printStackTrace();
			} catch (Exception e) {
				PluginLogger.severe(node, "读取配置文件时(#processField-" + instance.getClass().getSimpleName() + "."
						+ field.getName() + ")出现异常.");
				e.printStackTrace();
			}
		}
		return instance;

	}

	/**
	 * 反序列化容器类型中的元素.
	 * 
	 * @param node
	 * @param origin
	 * @param type
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object remapContainerElement(LogNode node, Object origin, Class<?> type) {
		if (String.class == type) { // String 类型
			return origin;
		} else if (SubConf.class.isAssignableFrom(type)) { // 配置子表
			return read(node, (Class) type, (Map) origin);
		}
		// won't happen
		throw new RuntimeException();
	}

}
