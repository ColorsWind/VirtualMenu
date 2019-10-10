package com.blzeecraft.virtualmenu.core.config.template;

import static com.blzeecraft.virtualmenu.core.config.object.DataType.STRING_LIST;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.config.object.ObjectNode;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;

import lombok.val;

public class EventTemplate implements ITemplate<EventHandler> {
	
	@ObjectNode(key = "action", type = STRING_LIST)
	public Optional<List<String>> action;
	
	@ObjectNode(key = "condition", type = STRING_LIST)
	public Optional<List<String>> condition;

	@Override
	public EventHandler apply(LogNode node) {
		val condition = Conditions.parse(node, condition.orElse(Collections.emptyList()));
		val action = Actions.parse(node, action.orElse(Collections.emptyList()));
		return new EventHandler(condition, action);

	}

}
