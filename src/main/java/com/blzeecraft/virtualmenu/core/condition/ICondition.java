package com.blzeecraft.virtualmenu.core.condition;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import com.blzeecraft.virtualmenu.core.IScope;
import com.blzeecraft.virtualmenu.core.MenuEvent;

/**
 * 
 * @author colors_wind
 * @date 2020-02-11
 */
public interface ICondition extends Function<MenuEvent, Optional<String>>, Predicate<MenuEvent>, IScope {


	/**
	 * 检查事件是否满足条件,
	 * 这个方法通常会在检查view-condition时被调用.<p>
	 * 通常情况下, 这个方法不需要手动实现, 只需要实现 {@link #apply(MenuEvent)} 即可.
	 * 
	 * @see #apply(MenuEvent)
	 * @param event 待测事件
	 * @return true 如果满足条件, 否则返回 false.
	 */
	@Override
	default boolean test(MenuEvent event) {
		return !this.apply(event).isPresent();
	}
	
	/**
	 * 检查事件是否满足条件,
	 * 这个方法通常会在检查click-condition时被调用.
	 * 
	 * 
	 * @param event 待测事件
	 * @return {@link Optional#empty()} 如果满足条件, 否则返回非空 {@link Optional},
	 * 这种情况下可用 {@link Optional#get()} 获取不满足条件的原因.
	 */
	@Override
	Optional<String> apply(MenuEvent event);
	
}
