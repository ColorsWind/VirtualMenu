package com.blzeecraft.virtualmenu.core.variable;

import java.util.List;

import com.blzeecraft.virtualmenu.core.user.IUser;

public interface IVariableAdapter {
	
	/**
	 * 更新变量
	 * @param user
	 * @param line
	 * @return
	 */
	String replace(IUser<?> user, String line);

	/**
	 * 更新变量
	 * @param user
	 * @param lines
	 * @return
	 */
	List<String> replace(IUser<?> user, List<String> lines);
	
	/**
	 * 判断是否包含变量
	 * @param line
	 * @return
	 */
	boolean containsVariable(String line);
	
	/**
	 * 判断是否包含变量
	 * @param line
	 * @return
	 */
	boolean containsVariable(List<String> lines);
}
