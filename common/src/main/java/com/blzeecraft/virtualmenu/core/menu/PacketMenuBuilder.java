package com.blzeecraft.virtualmenu.core.menu;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.blzeecraft.virtualmenu.core.action.ActionUtils;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.icon.EmptyIcon;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.variable.UpdatePeriod;

import lombok.NonNull;

public class PacketMenuBuilder {

	protected final LogNode node;

	protected IMenuType type;
	protected Icon[] icons;
	protected Map<EventType, Consumer<MenuEvent>> menuAction;
	protected UpdatePeriod updateDelay;
	protected String title;

	public PacketMenuBuilder(LogNode node) {
		this.node = node;
		this.icons = new Icon[0];
		this.menuAction = new EnumMap<>(EventType.class);

	}

	public PacketMenuBuilder type(@NonNull IMenuType type) {
		this.type = type;
		this.icons = new Icon[type.getSize()];
		return this;
	}

	public PacketMenuBuilder title(@NonNull String title) {
		this.title = title;
		return this;
	}

	public PacketMenuBuilder refresh(UpdatePeriod delay) {
		this.updateDelay = delay;
		return this;
	}

	public PacketMenuBuilder setIcon(int slot, Icon icon) {
		if (slot < icons.length) {
			icons[slot] = icon;
			return this;
		}
		throw new IllegalArgumentException("尝试访问菜单不存在的icon的位置, length=" + icons.length + " slot=" + slot);
	}

	public PacketMenuBuilder icon(Icon[] icons) {
		this.icons = icons;
		return this;
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

	public PacketMenuBuilder addEventHandler(@NonNull EventType type, @NonNull Consumer<MenuEvent> handler) {
		menuAction.put(type, handler);
		return this;
	}

	public PacketMenu build() {
		// fill
		if (icons.length != type.getSize()) {
			this.icons = new Icon[type.getSize()];
			System.arraycopy(icons, 0, this.icons, 0, this.icons.length);
		}
		IntStream.range(0, this.type.getSize()).forEach(i -> {
			if (this.icons[i] == null) {
				icons[i] = EmptyIcon.INSTANCE;
			}
		});
		Arrays.stream(EventType.values()).forEach(eventType -> menuAction.putIfAbsent(eventType, ActionUtils.EMPTY_ACTION));
		return new PacketMenu(node, updateDelay, title, type, icons, menuAction);
	}

}
