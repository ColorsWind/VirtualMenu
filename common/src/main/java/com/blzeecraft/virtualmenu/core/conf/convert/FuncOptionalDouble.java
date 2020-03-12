package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.OptionalDouble;
import java.util.function.Function;

import lombok.NonNull;

public class FuncOptionalDouble implements Function<Object, OptionalDouble> {

	@Override
	@NonNull
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
