package com.blzeecraft.virtualmenu.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectUtils {
	
	public static Material getMaterial(String name) {
		try {
			int id = Integer.valueOf(name);
			Method m = Material.class.getMethod("getMaterial", int.class);
			Material type =  (Material) m.invoke(null, id);
			if (type != null) {
				return type;
			}
		} catch (NoSuchMethodException | NumberFormatException e) {
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return Material.valueOf(name);
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
				return ((Collection<?>)method.invoke(null)).toArray(new Player[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Player[0];
	}
	
	public static ItemStack asNMSCopy(ItemStack original) {
		try {
			Class<?> clazz = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".inventory.CraftItemStack");
			Method method = clazz.getMethod("asCraftCopy", ItemStack.class);
			return (ItemStack) method.invoke(null, original);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return original;
	}
	


}
