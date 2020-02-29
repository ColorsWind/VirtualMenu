package com.blzeecraft.virtualmenu.core.menu;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.action.ActionUtils;
import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.animation.EnumUpdateDelay;
import com.blzeecraft.virtualmenu.core.icon.EmptyIcon;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.user.UserSession;
import com.blzeecraft.virtualmenu.core.variable.VariableUpdater;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

/**
 * 代表一个菜单
 * 
 * @author colors_wind
 *
 */
@ToString
public abstract class AbstractPacketMenu implements IPacketMenu {
	@Getter
	protected final String title;
	@Getter
	protected final IMenuType type;
	@Getter
	protected final EnumUpdateDelay updateDelay;
	
	protected final Icon[] icons; // not null
	protected final Map<EventType, Consumer<MenuEvent>> menuAction; // not null

	protected final Set<UserSession> sessions;

	public AbstractPacketMenu(EnumUpdateDelay updateDelay, String title, IMenuType type) {
		this(updateDelay, title, type, new Icon[type.getSize()], new EnumMap<>(EventType.class));
	}

	public AbstractPacketMenu(EnumUpdateDelay updateDelay, String title, IMenuType type, Icon[] icons,
			Map<EventType, ? extends Consumer<MenuEvent>> events) {
		this.updateDelay = updateDelay;
		this.title = title;
		this.type = type;
		this.icons = new Icon[type.getSize()];
		System.arraycopy(icons, 0, this.icons, 0, this.icons.length);
		this.sessions = new HashSet<>();
		this.menuAction = new EnumMap<>(events);

		// fill
		Arrays.stream(EventType.values()).forEach(key -> this.menuAction.putIfAbsent(key, ActionUtils.EMPTY_ACTION));
		IntStream.range(0, this.icons.length).forEach(i -> {
			if (this.icons[i] == null) {
				this.icons[i] = EmptyIcon.INSTANCE;
			}
		});
	}

	@Override
	public void handle(IconActionEvent event) {
		val index = event.getSlot();
		if (index < icons.length) {
			val icon = icons[index];
			if (icon != null) {
				icon.accept(event);
			}
		}
	}

	@Override
	public void handle(MenuActionEvent event) {
		menuAction.get(event.getEventType()).accept(event);
	}

	@Override
	public Collection<UserSession> getSessions() {
		return Collections.unmodifiableSet(this.sessions);
	}

	@Override
	public void addViewer(UserSession session) {
		sessions.add(session);
		VariableUpdater.addUpdateTask(session);
	}

	@Override
	public void removeViewer(UserSession session) {
		sessions.remove(session);
		VariableUpdater.removeUpdateTask(session);
	}

	@Override
	public AbstractItem<?> viewItem(UserSession session, int slot) {
		if (slot < icons.length) {
			return icons[slot].view(session);
		}
		return VirtualMenu.emptyItem();

	}

	public Optional<Icon> viewIcon(UserSession session, int slot) {
		if (slot < icons.length) {
			val icon = icons[slot];
			if (icon != null) {
				return Optional.ofNullable(icons[slot]);
			}
		}
		return Optional.empty();
	}

	

}
