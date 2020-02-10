package com.blzeecraft.virtualmenu.core.menu;

import java.util.Collection;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.animation.EnumUpdateDelay;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.val;

/**
 * 数据包菜单的接口
 * @author colors_wind
 *
 */
public interface IPacketMenu {

	String getTitle();

	int getWindowId();

	IMenuType getType();

	default int getSize() {
		return getType().size();
	}

	void handle(IconActionEvent event);
	
	void handle(MenuActionEvent event);

	boolean addViewer(IUser<?> user);

	boolean removeViewer(IUser<?> user);

	Collection<IUser<?>> getViewers();

	AbstractItem<?> viewItem(IUser<?> user, int slot);
	
	default EnumUpdateDelay getUpdateDelay() {
		return EnumUpdateDelay.NEVER;
	}

	default AbstractItem<?>[] viewItems(IUser<?> user) {
		int size = getSize();
		val items = new AbstractItem<?>[size];
		for (int i = 0; i < size; i++) {
			items[i] = viewItem(user, i);
		}
		return items;
	}

	/**
	 * 获取菜单是否只有储存的功能
	 * 
	 * @return {@code true} 如果菜单只有储存功能, 否则返回 {@code false}
	 */
	default boolean isStoreOnly() {
		return getType().isItemMenu();
	}

	default void update(IUser<?> user, int slot) {
		VirtualMenu.createPacketSetSlot(user, getWindowId(), slot, viewItem(user, slot)).send();
	}

	default void update(IUser<?> user) {
		VirtualMenu.createPacketWindowItems(user, getWindowId(), viewItems(user)).send();
	}

}
