package com.blzeecraft.virtualmenu.builder;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.settings.Settings;
import com.blzeecraft.virtualmenu.utils.NBTUtils;
import com.blzeecraft.virtualmenu.utils.ReflectUtils;

import lombok.Getter;

@Getter
public class MenuBuilder {
	public static final String EDITOR = "Edit menu: ";
	public static final String NEW_MENU = "New menu...";
	@Getter
	protected static MenuBuilder instance;

	public static MenuBuilder init(VirtualMenuPlugin pl) {
		return instance = new MenuBuilder(pl);
	}

	private final File dir;
	private final VirtualMenuPlugin pl;
	private final MenuHandler handler;

	public MenuBuilder(VirtualMenuPlugin pl) {
		this.pl = pl;
		this.handler = new MenuHandler(this);
		this.dir = new File(pl.getDataFolder(), "builder");
		checkDir();
	}
	
	public void register() {
		Bukkit.getPluginManager().registerEvents(handler, pl);
	}

	public void checkDir() {
		if (dir.exists()) {
			dir.mkdir();
		}
	}
	
	public Inventory createInventory(Player p) {
		return this.createInventory(p, NEW_MENU, 54, new  MenuHolder());
	}

	public Inventory createInventory(Player p, ChestMenu menu) {
		Inventory inv = this.createInventory(p, EDITOR + menu.getName(), menu.getSlots(), new MenuHolder(menu.getName(), menu.getTitle()));
		inv.setContents(menu.getContents());
		return inv;
	}

	public Inventory createInventory(Player p, String title, int slot, MenuHolder holder) {
		Inventory inv = Bukkit.createInventory(holder, slot, title);
		return inv;
	}
	
	


	@SuppressWarnings("deprecation")
	public String saveInventory(MenuHolder holder, Inventory inv, String name) {
		name = fillName(name);
		File f = new File(dir, name);
		FileConfiguration cs = YamlConfiguration.loadConfiguration(dir);
		ConfigurationSection mSettings = cs.createSection("menu-settings");
		String title = holder.getTitle();
		mSettings.set("title",  title == null ? "" : title);
		mSettings.set("row", inv.getSize() /9);
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null) {
				int y = (i / 9) + 1;
				int x = (i % 9) + 1;
				ConfigurationSection sub = cs.createSection("X" + x + "Y" + y);
				ItemStack item = contents[i];
				ItemMeta meta = item.getItemMeta();
				sub.set("ID", item.getType().name());
				sub.set("DATA-VALUE", item.getDurability());
				sub.set("AMOUNT", item.getAmount());
				if (meta != null) {
					if (meta.hasDisplayName()) {
						sub.set("NAME", meta.getDisplayName());
					}
					if (meta.hasLore()) {
						sub.set("LORE", meta.getLore());
					}
				}
				if (Settings.getInstance().isSupportNBT()) {
					sub.set("NBT", NBTUtils.itemNBTtoText(item));
				}
				sub.set("POSITION-X", x);
				sub.set("POSITION-Y", y);
				sub.set("KEEP-OPEN", true);
			}
		}
		saveMenus(cs, f);
		return name;
	}

	public void closeAll() {
		for (Player p : ReflectUtils.getPlayerOnline()) {
			Inventory inv = p.getOpenInventory().getTopInventory();
			if (inv != null) {
				InventoryHolder holder = inv.getHolder();
				if (holder != null && holder instanceof MenuHolder) {
					p.closeInventory();
				}
			}
		}
	}

	private String fillName(String name) {
		if (name == null || name.isEmpty()) {
			name = String.valueOf(System.currentTimeMillis());
		}
		if (!name.toLowerCase().endsWith(".yml")) {
			name = name + "." + System.currentTimeMillis() + ".yml";
		}
		return name;
	}

	private void saveMenus(FileConfiguration cs, File f) {
		try {
			cs.save(f);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}


}
