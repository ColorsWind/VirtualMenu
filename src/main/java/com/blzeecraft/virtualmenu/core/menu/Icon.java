package com.blzeecraft.virtualmenu.core.menu;

import java.util.Optional;

import com.blzeecraft.virtualmenu.core.IUser;

public interface Icon {
	
	/**
	 * 获取玩家显示的物品
	 * @param user
	 * @return 如果不显示任何物品, 返回 {@link Optional#empty} 否则返回应显示的物品
	 */
	Optional<AbstractItem<?>> view(IUser<?> user);
	
	/**
	 * 检查玩家是否能查看这个{@link Icon}
	 * @param user
	 * @return
	 */
	boolean canView(IUser<?> user);
	
	/**
	 * 检查玩家是否能点击这个{@link Icon}
	 * @param user 玩家
	 * @return 如果允许玩家点击,返回 {@link Optional#empty} 否则返回拒绝的信息
	 */
	Optional<String> canClick(IUser<?> user);

}
