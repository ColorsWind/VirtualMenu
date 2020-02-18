package com.blzeecraft.virtualmenu.core.menu;

/**
 * 菜单类型的接口
 * @author colors_wind
 *
 */
public interface IMenuType {
	
	int getTypeId();
	
	String getType();
	
	int size();

	boolean isItemMenu();
	
}
