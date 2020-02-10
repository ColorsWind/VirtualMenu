package com.blzeecraft.virtualmenu.core.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.UserSession;
import com.blzeecraft.virtualmenu.core.adapter.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.IconActionEvent;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

/**
 * 所有有关数据包的操作都通过 {@link PacketManager} 进行
 * 
 * @author colors_wind
 * @date 2020-02-10
 */
public class PacketManager {
	private final static ConcurrentMap<IUser<?>, UserSession> USER_SESSION = new ConcurrentHashMap<>();

	public static void openMenu(IUser<?> user, IPacketMenu menu) {
		VirtualMenu.getScheduler().runTaskGuaranteePrimaryThread(() -> openMenuUncheck(user, menu));
	}

	public static void openMenuUncheck(IUser<?> user, IPacketMenu menu) {
		user.closePacketMenu();
		IPacketAdapter adapter = VirtualMenu.getPacketAdapter();
		VirtualMenu.getScheduler().runTaskLater(() -> {
			AbstractPacketWindowOpen<?> open = adapter.createPacketWindOpen(user, menu.getWindowId(), menu.getType(),
					menu.getTitle());
			AbstractPacketWindowItems<?> items = adapter.createPacketWindowItems(user, menu.getWindowId(),
					menu.viewItems(user));
			try {
				open.send();
				items.send();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return;
			}
			UserSession session = new UserSession(menu);
			USER_SESSION.put(user, session);
			menu.click(new IconActionEvent());
		}, 3L);
	}

	public static void handleEvent(PacketMenuClickEvent event) {

	}

	public static void handleEvent(PacketMenuCloseEvent event) {

	}

}
