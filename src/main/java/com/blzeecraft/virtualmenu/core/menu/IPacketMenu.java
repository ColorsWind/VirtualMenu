package com.blzeecraft.virtualmenu.core.menu;

import java.util.Collection;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.animation.EnumUpdateDelay;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.PacketManager;

import lombok.val;

/**
 * 代表一个菜单
 * 
 * @author colors_wind
 *
 */
public interface IPacketMenu {

	/**
	 * 获取菜单的标题, 对于同一个 {@link UserSession} 返回值应当相同.
	 * 
	 * @return 标题
	 */
	String getTitle();

	/**
	 * 获取菜单的windowId, 对于同一个 {@link UserSession} 返回值应当相同.
	 * 
	 * @return windowId
	 */
	default int getWindowId() {
		return Byte.MAX_VALUE;
	}

	/**
	 * 获取菜单类型, 应当返回一个定值.
	 * 
	 * @return 菜单类型
	 */
	IMenuType getType();

	/**
	 * 获取菜单容量, 应当返回一个定值.
	 * 
	 * @return 菜单容量
	 */
	default int getSize() {
		return getType().size();
	}

	/**
	 * 处理 {@link IconActionEvent} 事件. 这个方法会由 {@link PacketManager} 调用.
	 * 有关封包拦截,处理,发送都已由 {@link PacketManager} 完成.
	 * 
	 * @param event 事件
	 */
	void handle(IconActionEvent event);

	/**
	 * 处理 {@link MenuActionEvent} 事件. 这个方法会由 {@link PacketManager} 调用.
	 * 有关封包拦截,处理,发送都已由 {@link PacketManager} 完成.
	 * 
	 * @param event 事件
	 */
	void handle(MenuActionEvent event);

	/**
	 * 处理用户打开菜单, 这个方法会在创建打开菜单 Packet 前由 {@link PacketManager} 调用. 这里应该向 注意
	 * {@link IPacketMenu#handle(MenuActionEvent)} 将稍后由 {@link PacketManager} 调用.
	 * 这里只需要对 {@link UserSession} 进行初始化.
	 * 
	 * @param session 用户会话
	 * @return 菜单内的 Icon.
	 */
	AbstractItem<?>[] addViewer(UserSession session);

	/**
	 * 处理用户关闭菜单, 这个方法会在创建关闭菜单封包前由 {@link PacketManager} 调用. 注意
	 * {@link IPacketMenu#handle(MenuActionEvent)} 在稍早些已由 {@link PacketManager} 调用.
	 * 这里不需要再处理这个.
	 * 
	 * @param session 用户会话
	 */
	void removeViewer(UserSession session);

	/**
	 * 获取当前打开这个菜单的用户. 最好覆盖默认方法以提高效率.
	 * 
	 * @return 用户
	 */
	default Collection<IUser<?>> getViewers() {
		return getSessions().stream().map(UserSession::getUser).collect(Collectors.toSet());
	}

	/**
	 * 获取当前打开这个菜单的用户会话.
	 * 
	 * @return 用户会话
	 */
	Collection<UserSession> getSessions();

	/**
	 * 获取指定slot客户端显示的物品(缓存).
	 * 
	 * @param session 用户会话
	 * @param slot    这个物品的slot
	 * @return 显示的物品
	 */
	AbstractItem<?> viewItem(UserSession session, int slot);

	/**
	 * 获取整个菜单客户端显示的物品. 最好覆盖默认方法以提高效率.
	 * 
	 * @param session
	 * @return
	 */
	default AbstractItem<?>[] viewItems(UserSession session) {
		int size = getSize();
		val items = new AbstractItem<?>[size];
		for (int i = 0; i < size; i++) {
			items[i] = viewItem(session, i);
		}
		return items;
	}

	/**
	 * 获取菜单的更新延时(间隔), 这个只对变量更新有效.
	 * 
	 * @return 更新延时(间隔)
	 */
	default EnumUpdateDelay getUpdateDelay() {
		return EnumUpdateDelay.NEVER;
	}

	/**
	 * 玩家点击菜单插件恢复客户端显示时是否应该更新(即恢复客户端显示)所有的项目. 这应该是一个常量.
	 * 如果菜单有特殊的功能,如合成台,熔炉等,为保证客户端正确显示, 必须返回 {@code true}. 其他情况可返回 {@code false},
	 * 这样插件只会更新直接相关联 Icon 和玩家背包中相应的项目.
	 * 
	 * @return {@code true} 如果需要更新所有项目, 否则返回 {@code false}
	 */
	default boolean totalUpdate() {
		return getType().isItemMenu();
	}

}
