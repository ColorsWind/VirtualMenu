package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.OptionalInt;
import java.util.function.Function;


public class FuncOptionalInt implements Function<Object, OptionalInt> {

	@Override
	public OptionalInt apply(Object obj) {
		if (obj == null) {
			return OptionalInt.empty();
		}
		try {
			return OptionalInt.of(Integer.parseInt(obj.toString()));
		} catch (NumberFormatException e) {
		}
		return OptionalInt.empty();
	}

}
