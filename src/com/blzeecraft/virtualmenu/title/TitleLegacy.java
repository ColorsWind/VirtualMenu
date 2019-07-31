package com.blzeecraft.virtualmenu.title;

import org.bukkit.entity.Player;

public class TitleLegacy implements ITitleAPI {

	@Override
	public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		p.sendMessage(title + "|" + subtitle);
	}

	@Override
	public void sendActionBar(Player p, String msg) {
		p.sendMessage(msg);
		
	}

	@Override
	public void sendHeaderAndFooter(Player p, String head, String foot) {
		p.sendMessage(head + "|" + foot);
		
	}

}
