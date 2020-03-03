package com.blzeecraft.virtualmenu.core.conf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.function.Function;

import lombok.Getter;
import lombok.ToString;

/**
 * 用于给 Optional 拆包.
 * 
 * @author colors_wind
 *
 */

@ToString
@Getter
public class OptionalMapper {

	public static final Set<Class<?>> SUPPORT_TYPE = new HashSet<>(
			Arrays.asList(String.class, Integer.class, Long.class, Double.class, OptionalInt.class, OptionalLong.class, OptionalDouble.class));
	public static final Function<OptionalInt, Object> MAPPER_INT = opt -> {
		if (opt.isPresent()) {
			return opt.getAsInt();
		}
		return null;
	};
	public static final Function<OptionalLong, Object> MAPPER_LONG = opt -> {
		if (opt.isPresent()) {
			return opt.getAsLong();
		}
		return null;
	};
	public static final Function<OptionalDouble, Object> MAPPER_DOUBLE = opt -> {
		if (opt.isPresent()) {
			return opt.getAsDouble();
		}
		return null;
	};
	
	private final Object value;
	
	public OptionalMapper(Object value) {
		this.value = value;
	}
	
	public Object apply() {
		Class<?> clazz = value.getClass();
		if (clazz == OptionalInt.class) {
			return MAPPER_INT.apply((OptionalInt) value);
		} else if (clazz == OptionalLong.class) {
			return MAPPER_LONG.apply((OptionalLong) value);
		} else if (clazz == OptionalDouble.class) {
			return MAPPER_DOUBLE.apply((OptionalDouble) value);
		}
		return value;
	}
	
}
