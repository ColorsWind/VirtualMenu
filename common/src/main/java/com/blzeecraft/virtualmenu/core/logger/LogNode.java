package com.blzeecraft.virtualmenu.core.logger;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表一个日志节点, 这个类是不可变的
 * @author colors_wind
 *
 */
@ToString
@EqualsAndHashCode
public final class LogNode {
	public static final String SPLIT = ">";
	public static final String LIST_SUFFIX = "!";
	public static final LogNode ROOT = new LogNode("");

	
	// 上级LogNode
	private final LogNode parent;
	// 当前LogNode的名称
	@NonNull private final String name;
	// 格式化输出的缓存
	@NonNull private final String cache;


	@NonNull
	private LogNode(String root) {
		this.name =  root;
		this.parent = this;
		this.cache = root;
	}

	@NonNull
	private LogNode(LogNode parent, String name) {
		this.parent = parent;
		this.name = name;
		this.cache = new StringBuilder(parent.cache).append(SPLIT).append(name).toString();
	}

	/**
	 * @return 上级LogNode
	 */
	public LogNode parent() {
		if (isRoot()) {
			throw new ArrayIndexOutOfBoundsException("根 LogNode 没有上级 LogNode");
		}
		return parent;
	}
	
	public boolean isRoot() {
		return parent == null;
	}

	public LogNode sub(String name) {
		return new LogNode(this, name);
	}
	
	public String getPrefix() {
		return cache;
	}

	public LogNode list() {
		return new LogNode(parent, name + LIST_SUFFIX);
	}

	/**
	 * 创建根LogNode
	 * @param root 根名称
	 */
	public static LogNode of(String name) {
		return new LogNode(name);
	}

}
