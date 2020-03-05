package com.blzeecraft.virtualmenu.core.conf.line;

import java.util.LinkedHashMap;

import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;

/**
 * 代表一个由解析的配置映射得到的对象
 * @author colors_wind
 *
 */
public abstract class LineConfigObject implements LoggerObject {

	protected final LogNode node;
	protected final ResolvedLineConfig rlc;

	
	public LineConfigObject(LogNode node, ResolvedLineConfig rlc) {
		this.rlc = rlc;
		this.node = node;
	}
	
	public LineConfigObject(LogNode node) {
		this(node, new ResolvedLineConfig(new LinkedHashMap<>()));
	}
	
	@Override
	public LogNode getLogNode() {
		return node;
	}  

}
