package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 代表一个由 {@link IUser} 触发的菜单事件.
 * @author colors_wind
 * @date 2020-02-10
 */
@Getter
@ToString
@AllArgsConstructor
public abstract class PacketMenuEvent {
	protected final UserSession session;
	

}
