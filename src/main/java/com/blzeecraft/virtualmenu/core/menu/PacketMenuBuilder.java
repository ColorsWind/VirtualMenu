package com.blzeecraft.virtualmenu.core.menu;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.icon.Icon;

import lombok.NonNull;

public class PacketMenuBuilder {
	
	protected final IMenuType type;
	protected final Icon[] icons;
	protected final Map<EventType, Consumer<ClickEvent>> events;
	
	protected int refresh;
	protected String title;

	
	public PacketMenuBuilder(IMenuType type) {
		this.type = type;
		this.icons = new Icon[type.size()];
		this.events = new EnumMap<>(EventType.class);
		
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
		for(int i=0;i<icons.length;i++) {
			if (icons[i] == null) {
				icons[i] = icon;
				return this;
			}
		}
		throw new IllegalArgumentException("该菜单已满,无法继续添加icon");
	}
	
	public PacketMenuBuilder addEventHandler(EventType type, Consumer<ClickEvent> handle) {
		events.put(type, handle);
		return this;
	}
	

	

}
