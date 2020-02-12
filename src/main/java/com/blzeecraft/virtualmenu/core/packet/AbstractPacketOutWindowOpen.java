package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import lombok.ToString;

/**
 * 代表使玩家打开菜单时服务端发给客户端的 Packet.
 * 
 * @author colors_wind
 * 
 */
@ToString(callSuper = true)
public abstract class AbstractPacketOutWindowOpen<T> extends AbstractWindowPacket<T> {

	public AbstractPacketOutWindowOpen(T handle) {
		super(handle);
	}

	public abstract void setTitle(String titile);

	public abstract String getTitle();

	public abstract void setMenuType(IMenuType menuType);

	public abstract IMenuType getMenuType();

}
