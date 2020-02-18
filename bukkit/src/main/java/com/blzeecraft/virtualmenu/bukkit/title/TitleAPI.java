package com.blzeecraft.virtualmenu.bukkit.title;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;



public class TitleAPI {
	public static final ITitleAPI API = getAPI();
	
	public static ITitleAPI getAPI() {
		// 较高版本的Bukkit已经自带了sendActionbar等方法
		try {
			Player.class.getMethod("sendActionBar", String.class);
			return new TitleBukkit();
		} catch (Exception e) {
		}
		// 较老版本的Bukkit使用ProtocolLib
		try {
			GameMode.class.getField("SPECTATOR");
			return new TitlePacket();
		} catch (Exception e) {
		}
		// 版本在1.7.10或以下的使用sendMessage代替
		return new TitleLegacy();
	}
	
	
	public static void sendTitle(Player player, String title, String subtitle) {
		API.sendTitle(player, title, subtitle,
				 Constants.TITLE_FADE, Constants.TITLE_STAY, Constants.TITLE_FADE);
	}
	
	public static void sendTitle(Player player, String title, String subtitle,
			int fadeIn, int stay, int fadeOut) {
		API.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
	}
	
	
	
	public static void sendActionBar(Player p, String msg) {
		API.sendActionBar(p, msg);
	}
	
	
	
	public static void sendHeaderAndFooter(Player p, String head, String foot) {
		API.sendHeaderAndFooter(p, head, foot);
	}

}
