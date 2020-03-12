package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.function.Function;

import lombok.NonNull;

public class FuncString implements Function<Object, String> {

	@Override
	@NonNull
	public String apply(@NonNull Object obj) {
		return obj.toString();
	}

}
