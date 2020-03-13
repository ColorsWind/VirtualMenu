package com.blzeecraft.virtualmenu.core.conf.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.conf.ObjectConvertException;

import lombok.NonNull;
import lombok.val;
import lombok.var;

public class FuncList implements BiFunction<Object, Class<?>, List<?>> {

	@Override
	public List<?> apply(Object obj, @NonNull Class<?> type) {
		if (obj == null) {
			return Collections.emptyList();
		}
		if (type == String.class) {
			return asStringList(obj);
		} else if (obj instanceof List){
			val list = (List<?>) obj;
			return list.stream().map(element -> ConvertFunctions.convertObject(element, type)).collect(Collectors.toList());
		} 
		throw new ObjectConvertException("无法将" + obj + "转换为类型 " + type + ".");	
	}
	

	private List<String> asStringList(Object obj) {
		// 以";"分隔, 支持转义"\;"
		val list = new ArrayList<String>();
		val chars = obj.toString().toCharArray();
		var cache = new StringBuilder();
		var escape = false;
		// 循环处理所有字符
		for (char c : chars) {
			if (escape) {
				escape = false;
				cache.append(c);
			} else if ('\\' == c) {
				escape = true;
			} else if (';' == c) {
				list.add(cache.toString());
				cache = new StringBuilder();
			} else {
				cache.append(c);
			}
		}
		// 处理最后一项
		if (cache.length() != 0) {
			list.add(cache.toString());
		}
		return list;
	}

}
