package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.user.UserSession;

/**
 * 表示一个空的 Icon.
 * @author colors_wind
 * @date 2020-02-13
 */
public final class EmptyIcon implements Icon {
	public static final Icon INSTANCE = new EmptyIcon();

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
