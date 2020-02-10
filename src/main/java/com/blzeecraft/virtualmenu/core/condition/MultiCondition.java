package com.blzeecraft.virtualmenu.core.condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import lombok.val;

/**
 * 代表多个条件,这个类是不可变的.
 * @author colors_wind
 * @date 2020-02-07
 */
public class MultiCondition implements ICondition {
	
	protected final List<ICondition> conditions;
	

	public MultiCondition(List<ICondition> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}

	@Override
	public Optional<String> apply(IconActionEvent e) {
		for(val condition : conditions) {
			val opt = condition.apply(e);
			if (opt.isPresent()) {
				return opt;
			}
		}
		return Optional.empty();
	}
	
	public List<ICondition> getConditions() {
		return Collections.unmodifiableList(conditions);
	}
	


}
