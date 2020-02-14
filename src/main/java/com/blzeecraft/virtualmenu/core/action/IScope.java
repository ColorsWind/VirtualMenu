package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.menu.ClickType;

public interface IScope {
	
	default ClickType[] scope() {
		return ClickType.values();
	}

}
