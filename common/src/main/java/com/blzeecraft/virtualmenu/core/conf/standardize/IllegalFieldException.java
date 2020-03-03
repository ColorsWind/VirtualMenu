package com.blzeecraft.virtualmenu.core.conf.standardize;

import java.lang.reflect.Field;

/**
 * 表示负责序列化/反序列化的类字段不合法
 * 
 * @author colors_wind
 *
 */
@SuppressWarnings("serial")
public class IllegalFieldException extends PluginExecption {

	public IllegalFieldException(Field field) {
		super("类型 " + field.getDeclaringClass().getSimpleName() + " 中有字段 " + field.getType() + " " + field.getName()
				+ " 不可参与序列化/反序列化.");
	}

}
