package com.blzeecraft.virtualmenu.command.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.PlayerSubCommand;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;
import com.blzeecraft.virtualmenu.utils.NBTUtils;
import com.blzeecraft.virtualmenu.utils.ReflectUtils;

import net.md_5.bungee.api.ChatColor;

@Args({})
@Name("info")
@Help("/vm info 查看手上物品信息")
public class CommandInfo extends PlayerSubCommand {

	public CommandInfo(VirtualMenuPlugin pl) {
		super(pl);
	}

	@Override
	public void onCommand(Player p, Object[] args) {
		String[] m = execute(ReflectUtils.getItemInHand(p, true));
		execute(p, m);
		execute(Bukkit.getConsoleSender(), m);
	}
	
	public void execute(CommandSender sender, String[] message) {
		sender.sendMessage(message);
	}
	
	@SuppressWarnings("deprecation")
	public String[] execute(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return new String[]{ChatColor.RED + "手上无物品"};
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> messages = new ArrayList<>(5);
		messages.add(ChatColor.AQUA + " NAME: " + buildName(meta.getDisplayName()));
		List<String> lore = buildLore(meta.getLore());
		if (lore.isEmpty()) {
			messages.add(ChatColor.AQUA + " LORE: []" );
		} else {
			messages.add(ChatColor.AQUA + " LORE: " );
			messages.addAll(lore);
		}
		
		messages.add(ChatColor.AQUA + " ID: " + item.getType().name());
		messages.add(ChatColor.AQUA + " DATA-VALUE: " + item.getDurability());
		messages.add(ChatColor.AQUA + " AMOUNT: " + item.getAmount());
		messages.add(ChatColor.AQUA + " NBT: \"" + NBTUtils.itemNBTtoText(item).replace("\r", "\\r").replace("\n", "\\n")  + "\"");
		return messages.toArray(new String[0]);
	} 
	
	private List<String> buildLore(List<String> lore) {
		if (lore == null || lore.isEmpty()) {
			return Collections.emptyList();
		} else {
			ArrayList<String> list = new ArrayList<>(lore.size());
			for(String s : lore) {
				list.add(("   -\'" + s + "'").replace('§', '&'));
			}
		}
		return lore;
	}


	private String buildName(String displayName) {
		if (displayName == null) {
			return "\'\'";
		}
		return ("'" + displayName + "'").replace('§', '&');
	}

}
