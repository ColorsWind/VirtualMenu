package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表使玩家打开菜单时服务端发给客户端的数据包
 * @author colors_wind
 *
 * @param <T> 被封装的数据包类型
 */
@NonNull
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketWindowOpen<T> extends AbstractPacket<T> {

	protected final int windowId;
	protected final String title;
	protected final IMenuType type;

	public AbstractPacketWindowOpen(T handle, IUser<?> user, int windowId, String title, IMenuType type) {
		super(handle, user);
		this.windowId = windowId;
		this.title = title;
		this.type = type;
	}

}
