package com.blzeecraft.virtualmenu.menu.iiem;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.bridge.EconomyBridge;
import com.blzeecraft.virtualmenu.bridge.PlayerPointsBridge;
import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.IConfig;
import com.blzeecraft.virtualmenu.config.Node;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.menu.iiem.RequireItem.Result;
import com.blzeecraft.virtualmenu.packet.PacketManager;
import com.blzeecraft.virtualmenu.settings.Settings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Icon extends ViewItem implements IConfig {

	// action
	@Node(key = "COMMAND", type = DataType.COMMAND_LIST)
	protected Map<ClickType, List<AbstractAction>> command;

	@Node(key = "PERMISSION", type = DataType.STRING)
	protected String permission;

	@Node(key = "PERMISSION-MESSAGE", type = DataType.STRING)
	protected String permissionMessage;

	@Node(key = "LEVEL", type = DataType.INT)
	protected int level;

	@Node(key = "PRICE", type = DataType.DOUBLE)
	protected double price;

	@Node(key = "PRICE-MESSAGE", type = DataType.STRING)
	protected String priceMessage;

	@Node(key = "POINTS", type = DataType.INT)
	protected int points;

	@Node(key = "POINTS-MESSAGE", type = DataType.STRING)
	protected String pointsMessage;

	@Node(key = "REQUIRED-ITEM", type = DataType.REQUIRED_ITEM)
	protected RequireItem required;

	@Node(key = "REQUIRED-ITEM-MESSAGE", type = DataType.STRING)
	protected String requiredMessage;

	@Node(key = "KEEP-OPEN", type = DataType.BOOLEAN)
	protected boolean keepOpen;
	
	//update
	@Node(key = "PLACEHOLDERAPI", type = DataType.BOOLEAN)
	protected boolean placeholderapi;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected final ChestMenu menu;

	public Icon(ChestMenu menu) {
		this.menu = menu;
	}
	
	public ItemStack getItem(Player p) {
		ItemStack item = cacheItem.clone();
		if (placeholderapi) {
			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				if (displayname != null) {
					meta.setDisplayName(PlaceholderAPI.setPlaceholders(p, displayname));
				}
				if (lore != null) {
					meta.setLore(PlaceholderAPI.setPlaceholders(p, lore));
				}
				
				item.setItemMeta(meta);
			}
		}
		return item;
	}
	
	public void onClick(Player p, ClickType type) {
		if (! canView(p)) {
			return;
		}
		if (permission != null && !p.hasPermission(permission)) {
			if (permissionMessage == null) {
				Settings.sendMessage(p, String.format(Settings.getInstance().getLang_noPermission(), permission));
			} else {
				p.sendMessage(permissionMessage);
			}
			return;
		}
		if (price > 0D) {
			if (EconomyBridge.hasValidEconomy()) {
				if (!EconomyBridge.takeMoney(p, price)) {
					if (priceMessage == null) {
						Settings.sendMessage(p, String.format(Settings.getInstance().getLang_noEnoughMoney(), price));
					} else {
						p.sendMessage(String.format(priceMessage, price));
					}

					return;
				}
			} else {
				Settings.sendMessage(p, String.format(Settings.getInstance().getLang_internalError(),
						"PRICE is set while economy plugin haven't installed"));
				return;
			}
		}
		if (points > 0D) {
			if (PlayerPointsBridge.hasValidPlugin()) {
				if (!PlayerPointsBridge.takePoints(p, points)) {
					if (pointsMessage == null) {
						Settings.sendMessage(p, String.format(Settings.getInstance().getLang_noEnoughMoney(), points));
					} else {
						p.sendMessage(String.format(pointsMessage, points));
					}

					return;
				}
			} else {
				Settings.sendMessage(p, String.format(Settings.getInstance().getLang_internalError(),
						"POINTS is set while playerpoints haven't installed"));
				return;
			}
		}
		if (required != null) {
			PlayerInventory inv = p.getInventory();
			Result result = required.take(inv);
			if (result.hasItem()) {
				result.take();
			} else {
				if (requiredMessage == null) {
					Settings.sendMessage(p, String.format(Settings.getInstance().getLang_noEnoughtItem(),
							required.amount, required.cacheItem.getI18NDisplayName()));
				} else {
					p.sendMessage(String.format(requiredMessage, required.amount));
				}
				return;
			}
		}
		if (command != null) {
			List<AbstractAction> cmds = command.get(type);
			if (cmds != null) {
				try {
					for (AbstractAction cmd : cmds) {
						cmd.execute(p);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!keepOpen) {
			PacketManager.getInstance().closeInventory(p, menu);
		}
	}

	public boolean hasAction() {
		return command != null;
	}
	
	@Override
	public void apply() throws IllegalArgumentException {
		super.apply();
		if (priceMessage != null) {
			priceMessage = ChatColor.translateAlternateColorCodes('&', priceMessage);
		}
		if (pointsMessage != null) {
			pointsMessage = ChatColor.translateAlternateColorCodes('&', pointsMessage);
		}
		if (requiredMessage != null) {
			requiredMessage = ChatColor.translateAlternateColorCodes('&', requiredMessage);
		}
	}


	
	@Override
	public String getLogPrefix() {
		return "#Icon";
	}


	

}
