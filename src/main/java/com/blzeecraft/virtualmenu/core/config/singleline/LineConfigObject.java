package com.blzeecraft.virtualmenu.core.config.singleline;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.google.gson.JsonObject;

/**
 * 代表一个由解析的配置映射得到的对象
 * @author colors_wind
 *
 */
public abstract class LineConfigObject implements LoggerObject {

	protected final LogNode node;

	
	public LineConfigObject(LogNode node, JsonObject json) {
		this(node);
	}
	
	public LineConfigObject(LogNode node) {
		this.node = node;
	}
	
	@Override
	public LogNode getLogNode() {
		return node;
	}  

}
