package com.blzeecraft.virtualmenu.core.packet;

import lombok.Getter;
import lombok.ToString;

/**
 * 代表使玩家关闭菜单时服务端发送给客户端的 Packet.
 * 
 * @author colors_wind
 *
 */
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketOutCloseWindow<T> extends AbstractWindowPacket<T> {

	public AbstractPacketOutCloseWindow(T packet) {
		super(packet);
	}
}
