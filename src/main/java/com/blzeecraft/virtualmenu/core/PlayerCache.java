package com.blzeecraft.virtualmenu.core;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.AbstractPacketMenu;

import lombok.ToString;

@ToString
public class PlayerCache {
	
	public final AbstractPacketMenu menu;
	public final ConcurrentMap<Icon, AbstractItem<?>> viewItem;
	public final ConcurrentMap<MultiIcon, Optional<Icon>> viewIcon;
	
	public PlayerCache(AbstractPacketMenu menu) {
		this.menu = menu;
		this.viewItem = new ConcurrentHashMap<>();
		this.viewIcon = new ConcurrentHashMap<>();
	}

}
