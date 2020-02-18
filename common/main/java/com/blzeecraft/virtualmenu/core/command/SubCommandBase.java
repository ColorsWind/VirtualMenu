package com.blzeecraft.virtualmenu.core.command;

import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.Getter;

public abstract class SubCommandBase {
	public static final Class<?>[] ACCEPT_ARGS = new Class[] { Integer.class, Long.class, Double.class, String.class,
			IUser.class, IPacketMenu.class, int.class, long.class, double.class };

	@Getter
	protected String[] name;

	public SubCommandBase(String[] name) {
		this.name = name;
	}

}
