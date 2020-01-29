package com.blzeecraft.virtualmenu.core.conf.standardize;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

/**
 * 表示负责序列化/反序列化的类字段不合法
 * @author colors_wind
 *
 */
@SuppressWarnings("serial")
public class IllegalFieldException extends PluginExecption {

	public IllegalFieldException(LogNode node, Class<?> clazz) {
		super(node, "类型 " + clazz.getSimpleName() + " 中有非法字段(请联系插件的作者)");
	}
	

}
