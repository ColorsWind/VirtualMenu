package com.blzeecraft.virtualmenu.bukkit.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.meowj.langutils.lang.LanguageHelper;

public class ItemUtils {
	
	public static String getDisplayname(ItemStack item) {
		if (item == null) {
			return null;
		}
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return null;
		}
		return meta.getDisplayName();
	}
	
	public static List<String> getLore(ItemStack item) {
		if (item == null) {
			return null;
		}
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return null;
		}
		return meta.getLore();
	}
	
	public static ItemStack setDisplayname(ItemStack item, String displayname) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayname);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack setLore(ItemStack item, String... lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack asNMSCopy(ItemStack original) {
		try {
			Class<?> clazz = Class
					.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack");
			Method method = clazz.getMethod("asCraftCopy", ItemStack.class);
			return (ItemStack) method.invoke(null, original);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;
	}
	
	
	public static Optional<Material> getMaterial(int id) throws NoSuchMethodException {
		try {
			Method m = Material.class.getMethod("getMaterial", int.class);
			return Optional.ofNullable((Material) m.invoke(null, id));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public static Optional<Material> getMaterial(String id){
		return Optional.ofNullable(Material.getMaterial(id));
	}
	
	public static String getItemName(ItemStack item) {
		if (item == null) {
			return "null";
		}
		// 使用LangUtils获取物品名称
		try {
			Class.forName("com.meowj.langutils.lang.LanguageHelper");
			return LanguageHelper.getItemName(item, "zh_CN");
		} catch (ClassNotFoundException e) {
		}
		try {
			ItemStack.class.getMethod("getI18NDisplayName", new Class<?>[0]);
			return item.getI18NDisplayName();
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return item.getType().name();
	}

}
