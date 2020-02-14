package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;

/**
 * 表示一个空的 Icon, 仅用于填充 {@link UserSession#viewIcon}.
 * @author colors_wind
 * @date 2020-02-13
 */
public class EmptyIcon implements Icon {

	@Override
	public AbstractItem<?> view(UserSession session) {
		return VirtualMenu.emptyItem();
	}

	@Override
	public boolean canView(UserSession session) {
		return false;
	}

	@Override
	public Optional<String> canClick(IconActionEvent e) {
		return Optional.of("找不到可见的Icon.");
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void accept(IconActionEvent e) {}

	@Override
	public AbstractItem<?> refreshItem(UserSession session) {
		return VirtualMenu.emptyItem();
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}

}
