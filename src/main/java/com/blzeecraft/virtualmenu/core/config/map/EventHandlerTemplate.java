package com.blzeecraft.virtualmenu.core.config.map;

import static com.blzeecraft.virtualmenu.core.config.map.DataType.STRING_LIST;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;

import lombok.val;

public class EventHandlerTemplate implements ITemplate<EventHandler> {
	
	@Node(key = "action", type = STRING_LIST)
	public Optional<List<String>> action;
	
	@Node(key = "condition", type = STRING_LIST)
	public Optional<List<String>> condition;

	@Override
	public EventHandler apply(LogNode node) {
		val condition = Conditions.parse(node, condition.orElse(Collections.emptyList()));
		val action = Actions.parse(node, action.orElse(Collections.emptyList()));
		return new EventHandler(condition, action);

	}

}
