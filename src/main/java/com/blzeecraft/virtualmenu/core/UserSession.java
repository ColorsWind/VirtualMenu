package com.blzeecraft.virtualmenu.core;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.ToString;

/**
 * 用于保存玩家目前打开菜单的相关信息.
 * 应在玩家成功打开菜单后由 {@link PacketManager} 创建.
 * 玩家每次打开菜单都需要创建一个新的 {@link UserSession}}
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
@ToString
public class UserSession {
	
	public final IPacketMenu menu;
	public final ConcurrentMap<Icon, AbstractItem<?>> viewItem;
	public final ConcurrentMap<MultiIcon, Optional<Icon>> viewIcon;
	
	public UserSession(IPacketMenu menu) {
		this.menu = menu;
		this.viewItem = new ConcurrentHashMap<>();
		this.viewIcon = new ConcurrentHashMap<>();
	}

}
