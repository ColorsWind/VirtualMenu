package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.OptionalLong;
import java.util.function.Function;

import lombok.NonNull;

public class FuncOptionalLong implements Function<Object, OptionalLong> {
	
	@Override
	@NonNull
	public OptionalLong apply(Object obj) {
		if (obj == null) {
			return OptionalLong.empty();
		}
		try {
			return OptionalLong.of(Long.parseLong(obj.toString()));
		} catch (NumberFormatException e) {
		}
		return OptionalLong.empty();
	}


}
