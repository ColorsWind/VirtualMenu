package com.blzeecraft.virtualmenu.core.animation;

import lombok.Getter;
import lombok.ToString;

/**
 * 用于标记 {@link com.blzeecraft.virtualmenu.icon.Icon} 变量的更新间隔.
 * @author colors_wind
 * @date 2020-02-10
 */
@ToString
public enum EnumUpdateDelay {
	
	FAST(5L),NORMAL(10L),SLOW(20L),VERY_SLOT(50L),NEVER(-1L);
	
	@Getter
	private final long delay;
	
	private EnumUpdateDelay(long delay) {
		this.delay = delay;
	}
	
	public boolean autoUpdate() {
		return this.delay > 0;
	}

}
