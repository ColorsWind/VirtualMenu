package com.blzeecraft.virtualmenu.core.menu;

import java.util.Collection;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.val;

public interface IPacketMenu {
	
	String getTitle();
	
	int getWindowId();
	
	IMenuType getType();
	
	default int getSize() {
		return getType().size();
	}
	
	void click(ClickEvent e);
	
	void update(IUser<?> user, int slot);
	
	default void update(IUser<?> user) {
		update(user, -1);
	}
	
	boolean addViewer(IUser<?> user);
	
	boolean removeViewer(IUser<?> user);
	
	Collection<IUser<?>> getViewers();
	
	AbstractItem<?> viewItem(IUser<?> user, int slot);
	
	default AbstractItem<?>[] viewItems(IUser<?> user) {
		int size = getSize();
		val items = new AbstractItem<?>[size];
		for(int i=0;i<size;i++) {
			items[i] = viewItem(user, i);
		}
		return items;
	}


	/**
	 * 获取菜单是否只有储存的功能
	 * @return {@code true} 如果菜单只有储存功能, 否则返回 {@code false}
	 */
	default boolean isStoreOnly() {
		return getType().isItemMenu();
	}

}
