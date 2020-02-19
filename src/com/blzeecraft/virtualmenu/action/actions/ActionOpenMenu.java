package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.menu.MenuManager;
import com.blzeecraft.virtualmenu.packet.PacketManager;
import com.blzeecraft.virtualmenu.settings.Settings;

import me.clip.placeholderapi.PlaceholderAPI;

public class ActionOpenMenu extends AbstractAction {
	private final String menu;

	public ActionOpenMenu(ILog parent, String raw) {
		super(parent, raw, ActionType.OPEN_MENU);
		menu = raw;

	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		String text = raw;
		if (isPlaceholderAPI) {
			text = PlaceholderAPI.setPlaceholders(p, text);
		}
		ChestMenu menu = MenuManager.getInstance().getMenu(this.menu);
		if (menu == null) {
			PluginLogger.warning(parent, "找不到菜单: " + text);
			Settings.sendMessage(p, "找不到菜单: " + text + ", 请与管理员联系.");
		} else {
			PacketManager.getInstance().openInventory(menu, p);
		}
	}

}
