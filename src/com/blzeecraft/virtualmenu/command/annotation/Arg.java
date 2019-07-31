package com.blzeecraft.virtualmenu.command.annotation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.command.IArgConverter;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.menu.MenuManager;

public enum Arg {
	
	PLAYER(new IArgConverter<Player>() {

		
		@Override
		public Player convert(String origin) {
			return Bukkit.getPlayerExact(origin);
		}

		@Override
		public String notVaild(String origin) {
			return "找不到玩家: " + origin;
		}}),
	MENU(new IArgConverter<ChestMenu>() {

		@Override
		public ChestMenu convert(String origin) {
			return MenuManager.getInstance().getMenu(origin);
		}

		@Override
		public String notVaild(String origin) {
			return "找不到菜单: " + origin;
		}}),
	STRING(new IArgConverter<String>() {

		@Override
		public String convert(String origin) {
			return origin;
		}

		@Override
		public String notVaild(String origin) {
			return null;
		}});
	
	private final IArgConverter<?> ctr;

	Arg(IArgConverter<?> ctr) {
		this.ctr = ctr;
	}

	public Object convert(String origin) {
		return this.ctr.convert(origin);
	}
	
	public String getErrorMessage(String origin) {
		return this.ctr.notVaild(origin);
	}

}
