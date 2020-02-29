package com.blzeecraft.virtualmenu.core.variable;

import java.util.List;

import com.blzeecraft.virtualmenu.core.user.IUser;

public class EmptyVariableAdapter implements IVariableAdapter {

	@Override
	public String replace(IUser<?> user, String line) {
		return line;
	}

	@Override
	public List<String> replace(IUser<?> user, List<String> lines) {
		return lines;
	}

	@Override
	public boolean containsVariable(String line) {
		return false;
	}

	@Override
	public boolean containsVariable(List<String> lines) {
		return false;
	}

}
