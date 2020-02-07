package com.blzeecraft.virtualmenu.core.menu;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.NonNull;

public class PacketMenuBuilder {

	protected final LogNode node;

	protected IMenuType type;
	protected Icon[] icons;
	protected Map<EventType, Consumer<ClickEvent>> events;
	protected int refresh;
	protected String title;

	public PacketMenuBuilder(LogNode node) {
		this.node = node;
		this.icons = new Icon[0];
		this.events = new EnumMap<>(EventType.class);

	}

	public PacketMenuBuilder type(@NonNull IMenuType type) {
		this.type = type;
		this.icons = new Icon[type.size()];
		return this;
	}

	public PacketMenuBuilder title(@NonNull String title) {
		this.title = title;
		return this;
	}

	public PacketMenuBuilder refresh(int refresh) {
		if (refresh <= 0) {
			this.refresh = -1;
		} else {
			this.refresh = refresh;
		}
		return this;
	}

	public PacketMenuBuilder setIcon(int slot, Icon icon) {
		if (slot < icons.length) {
			icons[slot] = icon;
			return this;
		}
		throw new IllegalArgumentException("尝试访问菜单不存在的icon的位置, length=" + icons.length + " slot=" + slot);

	}

	public PacketMenuBuilder addIcon(Icon icon) {
		for (int i = 0; i < icons.length; i++) {
			if (icons[i] == null) {
				icons[i] = icon;
				return this;
			}
		}
		throw new IllegalArgumentException("该菜单已满,无法继续添加icon");
	}

	public PacketMenuBuilder addEventHandler(@NonNull EventType type, @NonNull Consumer<ClickEvent> handle) {
		events.put(type, handle);
		return this;
	}

	public PacketMenu build() {
		return new PacketMenu(node, refresh, title, type, icons, events);
	}

}
