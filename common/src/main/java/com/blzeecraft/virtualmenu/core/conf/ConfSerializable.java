package com.blzeecraft.virtualmenu.core.conf;

import java.lang.reflect.Array;

import com.blzeecraft.virtualmenu.core.conf.transition.SubConf;

/**
 * 代表这个类是可序列化成 {@link SubConf} 的子类的.
 * @author colors_wind
 *
 * @param <T>
 */
public interface ConfSerializable<T extends SubConf> {
	
	T serialize();
	
	default T[] seriablizeAll() {
		T obj = serialize();
		@SuppressWarnings("unchecked")
		T[]	array = (T[]) Array.newInstance(obj.getClass(), 1);
		array[0] = obj;
		return array;
	}
}
