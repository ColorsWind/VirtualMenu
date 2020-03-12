package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.function.Function;

import lombok.NonNull;

public class FuncInteger implements Function<Object, Integer> {

	@Override
	@NonNull
	public Integer apply(@NonNull Object obj) {
		return Integer.valueOf(obj.toString());
	}

}
