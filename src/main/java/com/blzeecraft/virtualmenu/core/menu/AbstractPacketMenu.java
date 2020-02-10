package com.blzeecraft.virtualmenu.core.menu;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.MenuActionEvent;
import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;

import lombok.Getter;
import lombok.ToString;
import lombok.val;


/**
 * 代表一个菜单
 * @author colors_wind
 *
 */
@Getter
@ToString
public abstract class AbstractPacketMenu implements IPacketMenu {

	
	protected final int refresh;
	protected final String title;
	protected final IMenuType type;
	protected final Icon[] icons;
	protected final Map<EventType, ? extends Consumer<MenuActionEvent>> events;
	
	protected final Set<IUser<?>> viewers;

	public AbstractPacketMenu(int refresh, String title, IMenuType type) {
		this.refresh = refresh;
		this.title = title;
		this.type = type;
		this.icons = new Icon[type.size()];
		this.viewers = new HashSet<>();
		this.events = new EnumMap<>(EventType.class);
	}
	
	public AbstractPacketMenu(int refresh, String title, IMenuType type, Icon[] icons, Map<EventType, ? extends Consumer<MenuActionEvent>> events) {
		this.refresh = refresh;
		this.title = title;
		this.type = type;
		this.icons = new Icon[type.size()];
		System.arraycopy(icons, 0, this.icons, 0, this.icons.length);
		this.viewers = new HashSet<>();
		this.events = new EnumMap<>(events);
	}
	
	public void update(IUser<?> user, long tick, int slot) {
		if (refresh > 0 && tick % refresh == 0) {
			viewIcon(user, slot).ifPresent(icon -> {
				user.getPlayerCache().viewItem.replace(icon, icon.update(user));
				this.update(user, slot);
			});
		}
	}
	
	public void update(IUser<?> user, long tick) {
		if (refresh > 0 && tick % refresh == 0) {
			user.getPlayerCache().viewItem.replaceAll((k,v) -> k.update(user));
			this.update(user);
		}
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
		events.get(event.getEventType()).accept(event);
	}


	@Override
	public boolean addViewer(IUser<?> user) {
		this.viewers.add(user);
		user.setPlayerCache(new UserSession(this));
		return true;
	}

	@Override
	public boolean removeViewer(IUser<?> user) {
		this.viewers.remove(user);
		user.setPlayerCache(null);
		return true;
	}

	@Override
	public Collection<IUser<?>> getViewers() {
		return Collections.unmodifiableCollection(viewers);
	}

	@Override
	public AbstractItem<?> viewItem(IUser<?> user, int slot) {
		return viewIcon(user, slot).map(icon -> icon.view(user)).orElse(VirtualMenu.emptyItem());
	}

	public Optional<Icon> viewIcon(IUser<?> user, int slot) {
		if (slot < icons.length) {
			val icon = icons[slot];
			if (icon != null) {
				return Optional.ofNullable(icons[slot]);
			}
		}
		return Optional.empty();
	}


}
