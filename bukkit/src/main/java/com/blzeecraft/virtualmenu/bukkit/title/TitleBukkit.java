package com.blzeecraft.virtualmenu.bukkit.title;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

public class TitleBukkit implements ITitleAPI {

	@Override
	public void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		p.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		
	}

	@Override
	public void sendActionBar(Player p, String msg) {
		p.sendActionBar(msg);
		
	}

	@Override
	public void sendHeaderAndFooter(Player p, String head, String foot) {
		p.setPlayerListHeaderFooter(TextComponent.fromLegacyText(head), TextComponent.fromLegacyText(foot));
		
	}

}
