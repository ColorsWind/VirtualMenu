package com.blzeecraft.virtualmenu.core.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.MenuEvent;
import lombok.val;

/**
 * 用于包装多个条件, 这个类是不可变的.
 * 
 * @author colors_wind
 * @date 2020-02-07
 */
public class MultiCondition implements ICondition {
	
	protected final List<ICondition> conditions;
	
	/**
	 * 包装多个条件, 条件的顺序会被保留.
	 * @param conditions 多个条件
	 */
	public MultiCondition(List<ICondition> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}

	/**
	 * 按原顺序检查条件.
	 * @see #MultiCondition(List)
	 */
	@Override
	public Optional<String> apply(MenuEvent e) {
		for(val condition : conditions) {
			val opt = condition.apply(e);
			if (opt.isPresent()) {
				return opt;
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 获取包装的多个条件.
	 * @return 多个条件的视图, 且这个视图是不可变的.
	 */
	public List<ICondition> getConditions() {
		return Collections.unmodifiableList(conditions);
	}
	


}
