package com.blzeecraft.virtualmenu.core.animation;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.ToString;

/**
 * 用于标记 {@link com.blzeecraft.virtualmenu.icon.Icon} 变量的更新间隔.
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
@ToString
public enum EnumUpdateDelay {

	FAST(5L), NORMAL(10L), SLOW(20L), VERY_SLOT(50L), NEVER(-1L);

	@Getter
	private final long delay;

	private EnumUpdateDelay(long delay) {
		this.delay = delay;
	}

	public boolean autoUpdate() {
		return this.delay > 0;
	}

	public static Optional<EnumUpdateDelay> get(String refresh) {
		try {
			return Optional.of(valueOf(refresh));
		} catch (IllegalArgumentException | NullPointerException e) {
		}
		return Optional.empty();
	}

	public static String typesToString() {
		return Arrays.stream(values())
				.map(updateDelay -> new StringBuilder(updateDelay.name()).append("(")
						.append((String.format("%.2f", ((double) updateDelay.delay) / 20))).append("s)").toString())
				.reduce((s1, s2) -> String.join(", ", s1, s2)).orElse("null");
	}

}
