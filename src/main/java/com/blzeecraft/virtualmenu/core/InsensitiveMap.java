package com.blzeecraft.virtualmenu.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import lombok.val;

public class InsensitiveMap {
	
	public static <V> ConcurrentMap<String, V> wrap(ConcurrentMap<String, V> map) {
		return new ConcurrentMap<String, V>() {

			@Override
			public int size() {
				return map.size();
			}

			@Override
			public boolean isEmpty() {
				return map.isEmpty();
			}

			@Override
			public boolean containsKey(Object key) {
				return map.containsKey(toLowerCase(key));
			}

			@Override
			public boolean containsValue(Object value) {
				return map.containsKey(value);
			}

			@Override
			public V get(Object key) {
				return map.get(toLowerCase(key));
			}

			@Override
			public V put(String key, V value) {
				return map.put(toLowerCase(key), value);
			}

			@Override
			public V remove(Object key) {
				return map.remove(toLowerCase(key));
			}

			@Override
			public void putAll(Map<? extends String, ? extends V> m) {
				val newMap = new LinkedHashMap<String, V>();
				m.forEach((k,v) -> newMap.put(k, v));
				map.putAll(newMap);
			}

			@Override
			public void clear() {
				map.clear();
			}

			@Override
			public Set<String> keySet() {
				return map.keySet();
			}

			@Override
			public Collection<V> values() {
				return map.values();
			}

			@Override
			public Set<Entry<String, V>> entrySet() {
				return map.entrySet();
			}

			@Override
			public V putIfAbsent(String key, V value) {
				return map.putIfAbsent(toLowerCase(key), value);
			}

			@Override
			public boolean remove(Object key, Object value) {
				return map.remove(toLowerCase(key), value);
			}

			@Override
			public boolean replace(String key, V oldValue, V newValue) {
				return map.replace(toLowerCase(key), oldValue, newValue);
			}

			@Override
			public V replace(String key, V value) {
				return map.replace(toLowerCase(key), value);
			}};
	}
	
	public static <V> Map<String, V> wrap(Map<String, V> map) {
		return new Map<String, V>() {

			@Override
			public int size() {
				return map.size();
			}

			@Override
			public boolean isEmpty() {
				return map.isEmpty();
			}

			@Override
			public boolean containsKey(Object key) {
				return map.containsKey(toLowerCase(key));
			}

			@Override
			public boolean containsValue(Object value) {
				return map.containsValue(value);
			}

			@Override
			public V get(Object key) {
				return map.get(toLowerCase(key));
			}

			@Override
			public V put(String key, V value) {
				return map.put(toLowerCase(key), value);
			}

			@Override
			public V remove(Object key) {
				return map.remove(toLowerCase(key));
			}

			@Override
			public void putAll(Map<? extends String, ? extends V> m) {
				val newMap = new LinkedHashMap<String, V>();
				m.forEach((k,v) -> newMap.put(toLowerCase(k), v));
				map.putAll(newMap);
			}

			@Override
			public void clear() {
				map.clear();
			}

			@Override
			public Set<String> keySet() {
				return map.keySet();
			}

			@Override
			public Collection<V> values() {
				return map.values();
			}

			@Override
			public Set<Entry<String, V>> entrySet() {
				return map.entrySet();
			}
			
		};
			
	}
	
	public static Object toLowerCase(Object o) {
		if (o != null && o instanceof String) {
			return ((String) o).toLowerCase();
		}
		return o;
	}
	
	public static String toLowerCase(String s) {
		if (s != null) {
			return s.toLowerCase();
		}
		return null;
	}
	
}
