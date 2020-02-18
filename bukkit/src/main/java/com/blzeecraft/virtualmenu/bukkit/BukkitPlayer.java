package com.blzeecraft.virtualmenu.bukkit;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;

@AllArgsConstructor
public class BukkitPlayer implements IUser<Player> {
	
	protected final Player player;

	@Override
	public Player getHandle() {
		return this.player;
	}

	@Override
	public String getName() {
		return this.player.getName();
	}

	@Override
	public UUID getUniqueId() {
		return this.player.getUniqueId();
	}

	@Override
	public void sendMessage(BaseComponent... component) {
		this.player.spigot().sendMessage(component);
	}

	@Override
	public void sendMessage(String msg) {
		this.player.sendMessage(msg);
	}
	
	@Override
	public void sendMessage(String... msg) {
		this.player.sendMessage(msg);
	}
	

	@Override
	public void sendPluginMessage(String channel, byte[] byteArray) {
		this.player.sendPluginMessage(VirtualMenuPlugin.getInstance(), channel, byteArray);
	}

	@Override
	public boolean hasPermission(String permission) {
		return this.player.hasPermission(permission);
	}

	@Override
	public int getLevel() {
		return this.player.getLevel();
	}

	@Override
	public void setLevel(int level) {
		this.player.setLevel(level);
	}

	@Override
	public OptionalDouble getBanlance(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deposit(String currency, double amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdraw(String currency, double amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performCommand(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean performCommandAsAdmin(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendActionbar(String actionbar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(String sound, float volume, float pitch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<UserSession> getCurrentSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCurrentSession(UserSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConsole() {
		// TODO Auto-generated method stub
		return false;
	}


}
