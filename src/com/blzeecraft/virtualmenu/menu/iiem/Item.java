package com.blzeecraft.virtualmenu.menu.iiem;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.IConfig;
import com.blzeecraft.virtualmenu.config.Node;
import com.blzeecraft.virtualmenu.logger.PluginLogger;
import com.blzeecraft.virtualmenu.utils.NBTUtils;
import com.blzeecraft.virtualmenu.utils.XMaterial;

import lombok.Data;

@Data
public class Item implements IConfig {
	@Node(key = "ID", type = DataType.STRING)
	protected String type;

	@Node(key = "AMOUNT", type = DataType.INT)
	protected int amount;

	@Node(key = "DATA-VALUE", type = DataType.INT)
	protected int dataValue;

	@Node(key = "NAME", type = DataType.STRING)
	protected String displayname;

	@Node(key = "LORE", type = DataType.STRING_LIST)
	protected List<String> lore;

	@Node(key = "ENCHANTMENT", type = DataType.ENCHANTMENTS)
	protected Map<Enchantment, Integer> enchantments;
	
	@Node(key = "NBT", type = DataType.STRING)
	protected String nbt;
	
	protected ItemStack cacheItem;

	@SuppressWarnings("deprecation")
	@Override
	public void apply() throws IllegalArgumentException {
		if (displayname != null) {
			displayname = ChatColor.translateAlternateColorCodes('&', displayname);
		}
		if (lore != null) {
			for (int i = 0; i < lore.size(); i++) {
				lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
			}
		}
		if (amount == 0) {
			amount = 1;
		}
		if (type == null) {
			throw new IllegalArgumentException("Expect: ID: [Material(:byte)]  Set: null (NOT SET)");
		}
		StringTokenizer str = new StringTokenizer(type, ":");
		String name = str.nextToken();
		try {
			Material m = Material.valueOf(name);
			if (str.hasMoreTokens()) {
				byte data = Byte.parseByte(str.nextToken());
				cacheItem = new ItemStack(m, amount, data);
			} else {
				cacheItem = new ItemStack(m, amount);
			}
		} catch (IllegalArgumentException e) {
			PluginLogger.fine(this, "Cannot find: Material." + name + ". Try to use XMaterial to search.");
			XMaterial m = XMaterial.matchXMaterial(type);
			if (m == null) {
				throw new IllegalArgumentException("Expect: ID: [Material(:byte)]  Set: " + name + " (INVAILD MATERIAL)");
			} else {
				PluginLogger.fine(this, "Use Material." + m.parseMaterial() + " as type");
			}
			cacheItem = m.parseItem();
			cacheItem.setAmount(amount);
		}
		if (dataValue > 0) {
			cacheItem.setDurability((short) dataValue);
		}
		if (enchantments != null) {
			cacheItem.addUnsafeEnchantments(enchantments);
		}
		if (NBTUtils.isVaidNBT(nbt)) {
			try {
				cacheItem = NBTUtils.setItemNBT(cacheItem, nbt);
			} catch (IOException e) {
				PluginLogger.severe(this, "Error while setting item nbt: " + e.toString());
				e.printStackTrace();
			}
		}
		ItemMeta meta = cacheItem.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(displayname);
			meta.setLore(lore);
			cacheItem.setItemMeta(meta);
		}
	}

	@Override
	public String getLogPrefix() {
		return "#Item";
	}

}
