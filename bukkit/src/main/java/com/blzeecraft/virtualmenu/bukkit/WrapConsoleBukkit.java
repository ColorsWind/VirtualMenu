package com.blzeecraft.virtualmenu.bukkit;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

@ToString
@RequiredArgsConstructor
public class WrapConsoleBukkit implements IUser<ConsoleCommandSender> {
	
	public static final UUID CONSOLE_UUID = UUID.nameUUIDFromBytes(new byte[16]);
	protected final ConsoleCommandSender console;

	@Override
	public final ConsoleCommandSender getHandle() {
		return this.console;
	}

	@Override
	public String getName() {
		return "#Console";
	}

	@Override
	public UUID getUniqueId() {
		return CONSOLE_UUID;
	}

	@Override
	public void sendMessage(BaseComponent... components) {
		this.sendMessage(new TextComponent(components).toLegacyText());
	}

	@Override
	public void sendMessage(String msg) {
		this.sendMessage(msg);
		
	}

	@Override
	public void sendPluginMessage(String channel, byte[] byteArray) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public int getLevel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLevel(int level) {
		throw new UnsupportedOperationException();	
	}

	@Override
	public OptionalDouble getBanlance(String currency) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean deposit(String currency, double amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean withdraw(String currency, double amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean performCommand(String command) {
		return Bukkit.dispatchCommand(console, command);
	}

	@Override
	public boolean performCommandAsAdmin(String command) {
		return this.performCommand(command);
	}

	@Override
	public void sendActionbar(String actionbar) {
		this.sendMessage("#Actionbar: ", actionbar);
	}

	@Override
	public void sendTitle(String title) {
		this.sendMessage("#Title: ", title);
	}

	@Override
	public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		this.sendMessage("#Title: ", title , "|", "subTitle");
	}

	@Override
	public void playSound(String sound, float volume, float pitch) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<UserSession> getCurrentSession() {
		return Optional.empty();
	}

	@Override
	public void setCurrentSession(UserSession session) {}

	@Override
	public final boolean isConsole() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateInventory() {
		// TODO Auto-generated method stub
		
	}

}
