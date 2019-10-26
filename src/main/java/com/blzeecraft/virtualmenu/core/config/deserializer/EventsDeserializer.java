package com.blzeecraft.virtualmenu.core.config.deserializer;

import static com.blzeecraft.virtualmenu.core.config.node.DataType.STRING;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.config.node.ObjectNode;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;

import lombok.val;

public class EventsDeserializer implements IDeserializer<EventHandler> {
	
	@ObjectNode(key = "action", type = STRING)
	public Optional<List<String>> action;
	
	@ObjectNode(key = "condition", type = STRING)
	public Optional<List<String>> condition;

	@Override
	public EventHandler apply(LogNode node) {
		val condition = Conditions.parse(node, condition.orElse(Collections.emptyList()));
		val action = Actions.parse(node, action.orElse(Collections.emptyList()));
		return new EventHandler(condition, action);

	}

}
