package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.function.Function;

import lombok.NonNull;

public class FuncDouble implements Function<Object, Double> {

	@Override
	@NonNull
	public Double apply(@NonNull Object obj) {
		return Double.valueOf(obj.toString());
	}

}
