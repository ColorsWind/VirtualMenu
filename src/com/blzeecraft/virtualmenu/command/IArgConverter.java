package com.blzeecraft.virtualmenu.command;

public interface IArgConverter<T> {
	
	T convert(String origin);

	String notVaild(String origin);
}
