package com.blzeecraft.virtualmenu.core.command;

import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public final class Callstack {
	
	private final IUser<?> sender;
	private final String[] args;
	

}
