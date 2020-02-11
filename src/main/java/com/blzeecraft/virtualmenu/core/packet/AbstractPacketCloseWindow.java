package com.blzeecraft.virtualmenu.core.packet;

import lombok.Getter;
import lombok.ToString;

/**
 * 代表玩家关闭菜单时客户端发送给服务端的数据包
 * @author colors_wind
 *
 */
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketCloseWindow<T> extends AbstractWindowPacket<T> {

	public AbstractPacketCloseWindow(T packet) {
		super(packet);
	}
	

}
