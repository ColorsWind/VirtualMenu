package com.blzeecraft.virtualmenu.core.menu;

import java.util.Map;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.variable.UpdatePeriod;

public class PacketMenu extends AbstractPacketMenu implements LoggerObject {
	protected final LogNode node;
	

	public PacketMenu(LogNode node, UpdatePeriod delay, String title, IMenuType type, Icon[] icons,
			Map<EventType, Consumer<MenuEvent>> menuAction) {
		super(delay, title, type, icons, menuAction);
		this.node = node;
	}

	@Override
	public int getWindowId() {
		return 127;
	}

	@Override
	public LogNode getLogNode() {
		return node;
	}
	
	
	



}
