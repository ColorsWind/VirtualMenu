package com.blzeecraft.virtualmenu.core.icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.blzeecraft.virtualmenu.core.action.event.IconActionEvent;
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
	public AbstractItem<?> view(UserSession session) {
		return session.getCacheItem(session.getCacheIcon(this));
	}
	
	@Override
	public AbstractItem<?> refreshItem(UserSession session) {
		return session.getCacheIcon(this).refreshItem(session);
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

	/**
	 * 获取缓存的该用户显示的真实 Icon.
	 * @param session 用户会话
	 * @return Icon
	 */
	public Icon viewIcon(UserSession session) {
		return session.getCacheIcon(this);
	}

	@Override
	public int getPriority() {
		return 0;
	}


	public static MultiIcon of(Icon origin, Icon toCombined) {
		if (origin instanceof MultiIcon) {
			// flat
			List<Icon> originIcons = ((MultiIcon) origin).getIcons();
			List<Icon> newIcons = new ArrayList<>(originIcons.size() + 1);
			newIcons.addAll(originIcons);
			newIcons.add(toCombined);
			return new MultiIcon(newIcons);
		} else {
			return new MultiIcon(new Icon[] { origin, toCombined });
		}
	}

}
