package com.blzeecraft.virtualmenu.core.menu;

import java.util.Map;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;

public class PacketMenu extends AbstractPacketMenu implements LoggerObject {
	protected final LogNode node;

	public PacketMenu(LogNode node, int refresh, String title, IMenuType type, Icon[] icons,
			Map<EventType, ? extends Consumer<ClickEvent>> events) {
		super(refresh, title, type, icons, events);
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