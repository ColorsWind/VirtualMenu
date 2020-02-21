package com.blzeecraft.virtualmenu.bukkit.item;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.bukkit.WrapItemBukkit;
import com.blzeecraft.virtualmenu.bukkit.conf.Settings;
import com.blzeecraft.virtualmenu.core.item.AbstractItemBuilder;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;

import net.md_5.bungee.api.ChatColor;

public class BukkitItemBuilder extends AbstractItemBuilder<ItemStack> {
	public static final ItemStack FALLBACK = new ItemStack(Material.BEDROCK);
	static {
		ItemMeta meta = FALLBACK.getItemMeta();
		meta.setDisplayName("§c无效物品类型,请检查配置.");
		FALLBACK.setItemMeta(meta);
	}
	
	public BukkitItemBuilder() {
		this.id = FALLBACK.getType().name();
	}

	public BukkitItemBuilder(WrapItemBukkit bukkitItem) {
		this.id = bukkitItem.getId();
		this.amount = bukkitItem.getAmount();
		this.name = bukkitItem.getName();
		this.lore = bukkitItem.getLore();
		this.nbt = bukkitItem.getNbt();
	}

	@Override
	public WrapItemBukkit build(LogNode node) {
		// id not set
		if (this.id == null) {
			PluginLogger.severe(node, "未设置物品ID,请检查配置.");
			ItemStack item = ItemUtils.setDisplayname(FALLBACK.clone(), "未设置物品ID");
			return new WrapItemBukkit(item, "");
		}
		StringTokenizer tokenizer = new StringTokenizer(this.id, ":");
		String mainId = tokenizer.nextToken();
		byte dataId = tokenizer.hasMoreTokens() ? parseByte(tokenizer.nextToken()).orElseGet(() -> {
			PluginLogger.warning(node, "数据附加值 " + this.id.substring(mainId.length()) + " 无效,忽略.");
			return Byte.valueOf((byte) 0);
		}) : 0;

		Optional<ItemStack> optItem = parseInt(mainId).map(i -> buildItem(node, i, dataId))
				.orElseGet(() -> buildItem(node, mainId, dataId));
		ItemStack itemStack = optItem.map(item -> {
			Optional<String> itemName = Optional.ofNullable(this.name)
					.map(s -> ChatColor.translateAlternateColorCodes('&', s));
			Optional<List<String>> itemLore = Optional.ofNullable(this.lore).map(l -> l.stream()
					.map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList()));
			if (NBTUtils.isVaidNBT(nbt)) {
				try {
					item = NBTUtils.setItemNBT(item, nbt);
				} catch (Exception e) {
					PluginLogger.severe(node, "设置物品NBT时出现异常: " + e.toString());
					e.printStackTrace();
				}
			}
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				itemName.ifPresent(name -> meta.setDisplayName(name));
				itemLore.ifPresent(lore -> meta.setLore(lore));
			}
			return item;
		}).orElse(FALLBACK);
		return new WrapItemBukkit(itemStack, this.nbt);
	}
	
	private Optional<Byte> parseByte(String s) {
		try {
			return Optional.of(Byte.valueOf(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	private Optional<Integer> parseInt(String s) {
		try {
			return Optional.of(Integer.valueOf(s));
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	@SuppressWarnings("deprecation")
	public Optional<ItemStack> buildItem(LogNode node, String mainId, byte data) {
		Optional<Material> optMaterial = ItemUtils.getMaterial(mainId);
		Optional<ItemStack> buildItem = optMaterial.map(material -> {
			ItemStack item = new ItemStack(material);
			item.setDurability(data);
			return Optional.of(item);
		}).orElseGet(() -> {
			if (Settings.useXMaterial) {
				Optional<XMaterial> optXMaterial = XMaterial.matchXMaterial(mainId, data);
				Optional<ItemStack> optItem = optXMaterial.map(m -> {
					PluginLogger.warning(node, "无法精确ID =" +  mainId + "的物品, XMaterial使用搜索结果: " + m.parseMaterial().name() +", 可能不准确.");
					return Optional.of(optXMaterial.get().parseItem());
				}).orElseGet((() -> {
					PluginLogger.warning(node, "XMaterial无法找到 ID = " + mainId + ":" + data + " 的物品.");
					return Optional.empty();
				}));
				return optItem;
			} else {
				return Optional.empty();
			}
		});
		return buildItem;
	}

	@SuppressWarnings("deprecation")
	public Optional<ItemStack> buildItem(LogNode node, int mainId, byte data) {
		try {
			return ItemUtils.getMaterial(mainId).map(m -> {
				ItemStack item = new ItemStack(m);
				item.setDurability(data);
				return item;
			});
		} catch (NoSuchMethodException e) {
			PluginLogger.warning(node, "请注意: Minecraft(Version≥1.13), 已完全移除了对数字ID的支持.");
		}
		if (Settings.useXMaterial) {
			Optional<XMaterial> optMaterial = XMaterial.matchXMaterial(mainId, data);
			if (optMaterial.isPresent()) {
				PluginLogger.warning(node, "使用数字ID是不被推荐的. 使用XMaterial 查找到的物品可能不准确.");
				return Optional.of(optMaterial.get().parseItem());
			} else {
				PluginLogger.warning(node, "XMaterial无法找到 ID = " + +mainId + ":" + data + " 的物品.");
			}
		}
		return Optional.empty();
	}

}
