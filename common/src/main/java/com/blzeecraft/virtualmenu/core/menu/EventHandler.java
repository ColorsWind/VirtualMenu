package com.blzeecraft.virtualmenu.core.menu;

import java.util.Arrays;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.action.IAction;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.condition.ICondition;
import com.blzeecraft.virtualmenu.core.conf.ConfSerializable;
import com.blzeecraft.virtualmenu.core.conf.StringSerializable;
import com.blzeecraft.virtualmenu.core.conf.transition.StandardConf.EventConf;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventHandler implements Consumer<IconActionEvent> , ConfSerializable<EventConf> {
	protected final ICondition condition;
	protected final IAction action;
	
	
	@Override
	public void accept(IconActionEvent e) {
		if (condition.test(e)) {
			action.accept(e);
		}
		
	}


	@Override
	public EventConf serialize() {
		EventConf conf = new EventConf();
		if (condition instanceof StringSerializable) {
			conf.condition = Arrays.asList(((StringSerializable) condition).seriablizeAll());
		}
		if (action instanceof StringSerializable) {
			conf.action = Arrays.asList(((StringSerializable) action).seriablizeAll());
		}
		return conf;
	}


	@Override
	public EventConf[] seriablizeAll() {
		return new EventConf[] {serialize()};
	}

}
