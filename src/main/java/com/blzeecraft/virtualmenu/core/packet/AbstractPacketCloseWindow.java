package com.blzeecraft.virtualmenu.core.packet;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * 代表玩家关闭菜单时客户端发送给服务端的数据包
 * @author colors_wind
 *
 * @param <T> 被封装的数据包类型
 */
@NonNull
@Getter
@ToString(callSuper = true)
public abstract class AbstractPacketCloseWindow<T> extends AbstractPacket<T> {

	protected final int windowId;
	
	/**
	 * 如果该选项为true，拦截此数据包
	 */
	private boolean cancel;

	public AbstractPacketCloseWindow(T handle, IUser<?> user, int windowId) {
		super(handle, user);
		this.windowId = windowId;
	}

}
