package com.blzeecraft.virtualmenu.core.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

import lombok.val;

public class MultiCondition implements ICondition {
	
	protected final List<ICondition> conditions;

	public MultiCondition(List<ICondition> conditions) {
		this.conditions = new ArrayList<>(conditions);
	}

	@Override
	public Optional<String> apply(ClickEvent e) {
		for(val condition : conditions) {
			val opt = condition.apply(e);
			if (opt.isPresent()) {
				return opt;
			}
		}
		return Optional.empty();
	}

}
