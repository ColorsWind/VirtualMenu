package com.blzeecraft.virtualmenu.bukkit.title;

import org.bukkit.entity.Player;

public interface ITitleAPI {

	void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut);

	void sendActionBar(Player p, String msg);

	void sendHeaderAndFooter(Player p, String head, String foot);

}
