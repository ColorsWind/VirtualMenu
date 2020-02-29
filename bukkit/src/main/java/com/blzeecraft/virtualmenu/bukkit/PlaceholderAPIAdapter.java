package com.blzeecraft.virtualmenu.bukkit;

import java.util.List;

import com.blzeecraft.virtualmenu.core.user.IUser;
import com.blzeecraft.virtualmenu.core.variable.IVariableAdapter;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIAdapter implements IVariableAdapter {

	@Override
	public String replace(IUser<?> user, String line) {
		return PlaceholderAPI.setPlaceholders(((WrapPlayerBukkit)user).getHandle(), line);
	}

	@Override
	public List<String> replace(IUser<?> user, List<String> lines) {
		return PlaceholderAPI.setPlaceholders(((WrapPlayerBukkit)user).getHandle(), lines);
	}

	@Override
	public boolean containsVariable(String line) {
		return PlaceholderAPI.containsPlaceholders(line);
	}

	@Override
	public boolean containsVariable(List<String> lines) {
		return lines.stream().anyMatch(PlaceholderAPI::containsPlaceholders);
	}

}
