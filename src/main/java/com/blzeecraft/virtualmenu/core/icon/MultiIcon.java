package com.blzeecraft.virtualmenu.core.icon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickEvent;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

/**
 * 多个 {@link Icon} 复合,用于支持优先级显示,这个类是不可变的.
 * @author colors_wind
 *
 */
@ToString
public class MultiIcon implements Icon {
	@Getter
	protected final List<Icon> icons;

	public MultiIcon(List<Icon> icons) {
		this(icons.toArray(new Icon[0]));
	}

	public MultiIcon(Icon... icons) {
		val sort = Arrays.asList(icons);
		Collections.sort(sort);
		Collections.reverse(sort);
		this.icons = sort;
	}

	@Override
	public AbstractItem<?> view(IUser<?> user) {
		return viewIcon(user).map(icon -> icon.view(user)).orElse(VirtualMenu.emptyItem());

	}

	@Override
	public boolean canView(IUser<?> user) {
		return view(user).isEmpty();
	}

	@Override
	public Optional<String> canClick(ClickEvent e) {
		val user = e.getUser();
		return viewIcon(user).map(icon -> icon.canClick(e)).orElse(Optional.of("找不到可见的Icon"));
	}

	@Override
	public void accept(ClickEvent e) {
		viewIcon(e.getUser()).ifPresent(icon -> icon.accept(e));
	}

	public Optional<Icon> viewIcon(IUser<?> user) {
		return user.getPlayerCache().viewIcon.computeIfAbsent(this, k -> {
			for (val i : icons) {
				if (i.canView(user)) {
					return Optional.of(i);
				}
			}
			return Optional.empty();
		});

	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public AbstractItem<?> update(IUser<?> user) {
		return viewIcon(user).map(icon -> icon.update(user)).orElse(VirtualMenu.emptyItem());
	}

}
