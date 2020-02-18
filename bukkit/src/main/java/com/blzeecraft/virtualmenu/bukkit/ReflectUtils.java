package com.blzeecraft.virtualmenu.bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.meowj.langutils.lang.LanguageHelper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectUtils {

	public static String getNMSVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}

	public static Class<?> getNMSClass(String name) throws Exception {
		return Class.forName("net.minecraft.server." + getNMSVersion() + "." + name);
	}

	public static Material getMaterial(int id) {
		try {
			Method m = Material.class.getMethod("getMaterial", int.class);
			Material type = (Material) m.invoke(null, id);
			if (type != null) {
				return type;
			}
		} catch (NoSuchMethodException e) {
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	public static ItemStack getItemInHand(Player p, boolean b) {
		try {
			String name = b ? "getItemInMainHand" : "getItemInOffHand";
			PlayerInventory inv = p.getInventory();
			Class<? extends PlayerInventory> clazz = inv.getClass();
			Method method = null;
			try {
				method = clazz.getMethod(name);
			} catch (NoSuchMethodException e) {
				method = clazz.getMethod("getItemInHand");
			}
			return (ItemStack) method.invoke(inv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ItemStack(Material.AIR);
	}

	public static Player[] getPlayerOnline() {
		try {
			Method method = Bukkit.class.getMethod("getOnlinePlayers");
			if (method.getReturnType().isArray()) {
				return (Player[]) method.invoke(null);
			} else {
				return ((Collection<?>) method.invoke(null)).toArray(new Player[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Player[0];
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
