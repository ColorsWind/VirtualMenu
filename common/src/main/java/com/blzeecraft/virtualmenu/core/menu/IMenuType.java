package com.blzeecraft.virtualmenu.core.menu;

/**
 * 菜单类型的接口
 * @author colors_wind
 *
 */
public interface IMenuType {
	
	int getIndex();
	
	String getType();
	
	default int getLegacyIndex() {
		throw new UnsupportedOperationException();
	}
	
	default String getMinecraftKey() {
		throw new UnsupportedOperationException();
	}
	
	int getSize();

	boolean isItemMenu();


	
}
