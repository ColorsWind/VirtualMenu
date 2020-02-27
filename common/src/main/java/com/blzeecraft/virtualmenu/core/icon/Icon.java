package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.user.UserSession;
import com.blzeecraft.virtualmenu.core.icon.EmptyIcon;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

/**
 * 代表菜单中的一个图标, 单个图标可以为不同的玩家显示不同的物品.
 * Icon 不应该修改 {@link UserSession} 中的字段.
 * 所有对 UserSession 的中修改应该由 {@link PacketManager} 进行.
 * @author colors_wind
 *
 */
public interface Icon extends Comparable<Icon>, Consumer<IconActionEvent> {

	/**
	 * 获取玩家显示的物品.
	 * @param session
	 * @return 如果不显示任何物品, 返回 {@link VirtualMenu#emptyItem()} 否则返回应显示的物品
	 */
	AbstractItem<?> view(UserSession session);
	
	
	/**
	 * 检查玩家是否能查看这个 Icon.
	 * @param session UserSession
	 * @return 如果可以看见返回 {@code true}, 否则返回 {@code false}.
	 */
	boolean canView(UserSession session);
	
	/**
	 * 检查玩家是否能点击这个{@link Icon}
	 * @param e 点击的事件
	 * @return 如果允许玩家点击,返回 {@link Optional#empty} 否则返回拒绝的信息
	 */
	Optional<String> canClick(IconActionEvent e);
	
	/**
	 * 获取这个{@link Icon}显示的优先级
	 * @return 优先级
	 */
	int getPriority();
	
	void accept(IconActionEvent e);
	
	@Override
	default int compareTo(Icon o) {
		return Integer.compare(this.getPriority(), o.getPriority());
	}
	
	/**
	 * 该 Icon 是否为 {@link EmptyIcon}
	 * @return {@code true} 如果这里 Icon 为 {@link EmptyIcon}, 否则返回 {@code false}.
	 */
	default boolean isEmpty() {
		return false;
	}
	
	/**
	 * 获取该 Icon 对应的 LogNode.
	 * @return 对应的 LogNode.
	 */
	default LogNode getLogNode() {
		return LogNode.of("#Icon");
	}

	
	
	

}
