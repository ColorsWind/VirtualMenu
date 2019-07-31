package com.blzeecraft.virtualmenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InsensitiveMap<V> extends HashMap<String, V>{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2238029781890929781L;

	public static <U> InsensitiveMap<U> newInsensitiveMap() {
		return new InsensitiveMap<>();
	}

	@Override
	public V get(Object key) {
		if (key instanceof String) {
			return super.get(((String) key).toLowerCase());
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof String) {
			return super.containsKey(((String) key).toLowerCase());
		}
		return false;
	}

	@Override
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}



	@Override
	public V remove(Object key) {
		if (key instanceof String) {
			return super.remove(((String) key).toLowerCase());
		}
		return null;
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		if (key instanceof String) {
			return super.getOrDefault(((String) key).toLowerCase(), defaultValue);
		}
		return defaultValue;
	}

	@Override
	public V putIfAbsent(String key, V value) {
		return super.putIfAbsent(key.toLowerCase(), value);
	}

	@Override
	public boolean replace(String key, V oldValue, V newValue) {
		return super.replace(key.toLowerCase(), oldValue, newValue);
	}

	@Override
	public V replace(String key, V value) {
		return super.replace(key.toLowerCase(), value);
	}

	@Override
	public void replaceAll(BiFunction<? super String, ? super V, ? extends V> function) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
		return super.computeIfAbsent(key.toLowerCase(), mappingFunction);
	}

	@Override
	public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
		return super.computeIfPresent(key.toLowerCase(), remappingFunction);
	}

	@Override
	public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
		return super.compute(key.toLowerCase(), remappingFunction);
	}

	@Override
	public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return super.merge(key.toLowerCase(), value, remappingFunction);
	}

}
