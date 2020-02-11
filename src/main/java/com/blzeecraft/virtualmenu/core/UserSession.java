package com.blzeecraft.virtualmenu.core;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.Getter;
import lombok.ToString;

/**
 * 用于保存玩家目前打开菜单的相关信息.
 * 应在确认玩家打开菜单请求后, 创建发送给客户端的封包前, 由 {@link PacketManager} 创建.
 * 玩家每次打开菜单都需要创建一个新的 {@link UserSession}}
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
@ToString
public class UserSession {
	
	@Getter protected final IUser<?> user;
	@Getter protected final IPacketMenu menu;
	protected final ConcurrentMap<Icon, AbstractItem<?>> viewItem;
	protected final ConcurrentMap<MultiIcon, Optional<Icon>> viewIcon;
	
	public UserSession(IUser<?> user, IPacketMenu menu) {
		this.user = user;
		this.menu = menu;
		this.viewItem = new ConcurrentHashMap<>();
		this.viewIcon = new ConcurrentHashMap<>();
	}

}
