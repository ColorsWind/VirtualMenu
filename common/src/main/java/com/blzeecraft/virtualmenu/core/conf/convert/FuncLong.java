package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.function.Function;

import lombok.NonNull;

public class FuncLong implements Function<Object, Long> {

	@Override
	@NonNull
	public Long apply(@NonNull Object obj) {
		return Long.valueOf(obj.toString());
	}

}
