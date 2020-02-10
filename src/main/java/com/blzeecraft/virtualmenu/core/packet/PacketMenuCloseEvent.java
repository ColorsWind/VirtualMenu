package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import lombok.Getter;
import lombok.ToString;

/**
 * 代表 {@link IUser} 关闭菜单的事件.
 * 即使是因为退出游戏而关闭菜单,也应该触发这个事件.
 * 
 * @see {@link #isQuitGame()}
 * @author colors_wind
 * @date 2020-02-10
 */
@Getter
@ToString(callSuper = true)
public class PacketMenuCloseEvent extends PacketMenuEvent {
	protected final boolean quitGame;

	public PacketMenuCloseEvent(IUser<?> user, IPacketMenu menu, boolean quitGame) {
		super(user, menu);
		this.quitGame = quitGame;
	}
	

}
