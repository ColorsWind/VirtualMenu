package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.OptionalDouble;
import java.util.function.Function;


public class FuncOptionalDouble implements Function<Object, OptionalDouble> {

	@Override
	public OptionalDouble apply(Object obj) {
		if (obj == null) {
			return OptionalDouble.empty();
		}
		try {
			return OptionalDouble.of(Double.parseDouble(obj.toString()));
		} catch (NumberFormatException e) {
		}
		return OptionalDouble.empty();
	}

}
