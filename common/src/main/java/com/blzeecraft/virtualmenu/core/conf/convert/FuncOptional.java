package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.Optional;
import java.util.function.BiFunction;

import lombok.NonNull;

public class FuncOptional implements BiFunction<Object, Class<?>, Optional<?>> {

	@Override
	@NonNull
	public Optional<?> apply(Object obj, @NonNull Class<?> type) {
		return Optional.ofNullable(obj).map(element -> ConvertFunctions.convertObject(obj, type));
	}

}
