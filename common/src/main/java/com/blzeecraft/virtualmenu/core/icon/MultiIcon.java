package com.blzeecraft.virtualmenu.core.icon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
import com.blzeecraft.virtualmenu.core.conf.ConfSerializable;
import com.blzeecraft.virtualmenu.core.conf.standardize.StandardConf.IconConf;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.Getter;
import lombok.ToString;
import lombok.val;

/**
 * 多个 {@link Icon} 复合,用于支持优先级显示,这个类是不可变的.
 * 
 * @author colors_wind
 *
 */
@ToString
public class MultiIcon implements Icon, ConfSerializable<IconConf> {
	@Getter
	protected final List<Icon> storeIconList;

	public MultiIcon(List<Icon> icons) {
		// copy
		this(icons.toArray(new Icon[icons.size()]));
	}

	public MultiIcon(Icon... icons) {
		val sort = Arrays.asList(icons);
		Collections.sort(sort);
		Collections.reverse(sort);
		this.storeIconList = sort;
	}

	@Override
	public AbstractItem<?> view(UserSession session) {
		return viewIcon(session).view(session);
	}

	@Override
	public boolean canView(UserSession session) {
		return view(session).isEmpty();
	}

	@Override
	public Optional<String> canClick(IconActionEvent e) {
		return viewIcon(e.getSession()).canClick(e);
	}

	@Override
	public void accept(IconActionEvent e) {
		viewIcon(e.getSession()).accept(e);
	}

	public Icon viewIcon(UserSession session) {
		return storeIconList.stream().filter(icon -> icon.canView(session)).findFirst().orElse(EmptyIcon.INSTANCE);
	}

	@Override
	public int getPriority() {
		return 0;
	}

	public static MultiIcon of(Icon... icons) {
		List<Icon> flatMapIcons = Arrays.stream(icons).flatMap(icon -> {
			if (icon instanceof MultiIcon) {
				return ((MultiIcon) icon).storeIconList.stream();
			} else {
				return Stream.of(icon);
			}
		}).collect(Collectors.toList());
		return new MultiIcon(flatMapIcons);
	}

	@Override
	public IconConf serialize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IconConf[] seriablizeAll() {
		return storeIconList.stream().filter(ConfSerializable.class::isInstance).map(ConfSerializable.class::cast)
				.map(ConfSerializable::serialize).map(IconConf.class::cast).toArray(IconConf[]::new);
	}

}
