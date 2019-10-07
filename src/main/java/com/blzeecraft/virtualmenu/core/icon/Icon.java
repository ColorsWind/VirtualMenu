package com.blzeecraft.virtualmenu.core.icon;

import java.util.Optional;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

/**
 * 代表菜单中的一个图标, 单个图标可以为不同的玩家显示不同的物品.
 * @author colors_wind
 *
 */
public interface Icon extends Comparable<Icon>, Consumer<ClickEvent> {
	

	/**
	 * 获取玩家显示的物品
	 * @param user
	 * @return 如果不显示任何物品, 返回 {@link Optional#empty} 否则返回应显示的物品
	 */
	AbstractItem<?> view(IUser<?> user);
	
	/**
	 * 检查玩家是否能查看这个{@link Icon}
	 * @param user
	 * @return
	 */
	boolean canView(IUser<?> user);
	
	/**
	 * 检查玩家是否能点击这个{@link Icon}
	 * @param e 点击的事件
	 * @return 如果允许玩家点击,返回 {@link Optional#empty} 否则返回拒绝的信息
	 */
	Optional<String> canClick(ClickEvent e);
	
	/**
	 * 获取这个{@link Icon}显示的优先级
	 * @return 优先级
	 */
	int getPriority();
	
	void accept(ClickEvent e);
	
	@Override
	/**
	 * 升序排列
	 * @param o 另一个{@link Icon}
	 * @return 比较结果
	 */
	default int compareTo(Icon o) {
		return Integer.compare(this.getPriority(), o.getPriority());
	}

	AbstractItem<?> update(IUser<?> user);
	

}
