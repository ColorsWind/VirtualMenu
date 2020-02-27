package com.blzeecraft.virtualmenu.bukkit;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bukkit.economy.IEconomyHook;
import com.blzeecraft.virtualmenu.bukkit.title.TitleAPI;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.ToString;
import net.md_5.bungee.api.chat.BaseComponent;

@ToString
public class WrapPlayerBukkit implements IUser<Player> {

	protected final Player player;
	protected final VirtualMenuPlugin plugin;

	private volatile UserSession session;

	public WrapPlayerBukkit(VirtualMenuPlugin plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
	}

	@Override
	public final Player getHandle() {
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
		return this.plugin.getEconomy(currency).map(econ -> econ.getBanlance(player, currency))
				.orElse(OptionalDouble.empty());
	}

	@Override
	public boolean deposit(String currency, double amount) {
		Optional<IEconomyHook> econ = this.plugin.getEconomy(currency);
		if (econ.isPresent()) {
			return econ.get().deposit(player, currency, amount);
		}
		return false;
	}

	@Override
	public boolean withdraw(String currency, double amount) {
		Optional<IEconomyHook> econ = this.plugin.getEconomy(currency);
		if (econ.isPresent()) {
			return econ.get().withdraw(player, currency, amount);
		}
		return false;
	}

	@Override
	public boolean performCommand(String command) {
		return this.player.performCommand(command);
	}

	@Override
	public boolean performCommandAsAdmin(String command) {
		if (this.player.isOp()) {
			return this.performCommand(command);
		}
		player.setOp(true);
		boolean success = false;
		try {
			success = this.player.performCommand(command);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			player.setOp(false);
		}
		return success;
	}

	@Override
	public void sendActionbar(String actionbar) {
		TitleAPI.sendActionBar(player, actionbar);
	}

	@Override
	public void sendTitle(String title) {
		TitleAPI.sendTitle(player, title, "");

	}

	@Override
	public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
		TitleAPI.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);

	}

	@Override
	public void playSound(String sound, float volume, float pitch) {
		this.player.playSound(this.player.getLocation(), sound, volume, pitch);

	}

	@Override
	public Optional<UserSession> getCurrentSession() {
		return Optional.ofNullable(session);
	}

	@Override
	public void setCurrentSession(UserSession session) {
		this.session = session;
	}

	@Override
	public final boolean isConsole() {
		return false;
	}

	@Override
	public void updateInventory() {
		player.updateInventory();
	}

	@Override
	public Object[] getInventoryRawItems() {
		return player.getInventory().getContents();
	}

}
