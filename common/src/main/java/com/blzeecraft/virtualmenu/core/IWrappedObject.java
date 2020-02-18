package com.blzeecraft.virtualmenu.core;

/**
 * 对不同平台的实例进行包装
 * @author colors_wind
 *
 * @param <T> 相应的类型
 */
public interface IWrappedObject<T> {
	
	T getHandle();

}
