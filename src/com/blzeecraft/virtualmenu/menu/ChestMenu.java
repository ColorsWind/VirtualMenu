package com.blzeecraft.virtualmenu.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.IConfig;
import com.blzeecraft.virtualmenu.config.Node;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.menu.iiem.ExtendedIcon;
import com.blzeecraft.virtualmenu.menu.iiem.Icon;
import com.blzeecraft.virtualmenu.packet.EnumInventoryType;
import com.blzeecraft.virtualmenu.packet.PacketManager;

import lombok.Data;

@Data
public class ChestMenu implements IConfig {

	@Node(key = "name", type = DataType.STRING)
	protected String title;

	@Node(key = "rows", type = DataType.INT)
	protected int row;
	
	@Node(key = "auto-refresh", type = DataType.INT)
	protected int period;

	protected final String name;
	protected final ConcurrentMap<Integer, ExtendedIcon> icons;
	
	protected volatile BukkitTask updateTask;
	protected ConcurrentMap<Player, ViewPlayer> views;
	protected CopyOnWriteArrayList<ExtendedIcon> dynamic;

	public ChestMenu(String name) {
		this.icons = new ConcurrentHashMap<>();
		this.name = name;
		this.views = new ConcurrentHashMap<>();
		this.dynamic = new CopyOnWriteArrayList<>();
	}

	public boolean addIcon(ExtendedIcon icon) {
		return icons.put(icon.getSlot(), icon) == null;
	}

	public int getID() {
		return Byte.MAX_VALUE;
	}

	public EnumInventoryType getType() {
		return EnumInventoryType.CHEST;
	}

	public int getSlots() {
		return row * 9;
	}

	public String getName() {
		return name;
	}

	public void applyColor() {
		title = ChatColor.translateAlternateColorCodes('&', title);

	}

	public void click(int slot, Player p, ClickType type, ItemStack clickedItem) {
		ViewPlayer viewer = views.get(p);
		Icon icon = viewer.getIcon(slot);
		if (icon == null) {
			return;
		}
		if (!icon.hasAction()) {
			if (!icon.isKeepOpen()) {
				PacketManager.getInstance().closeInventory(p, this);
			}
			return;
		}
		if(viewer.canClick()) {
			if (Bukkit.isPrimaryThread()) {
				icon.onClick(p, type);
			} else {
				Bukkit.getScheduler().runTask(VirtualMenuPlugin.getInstance(), new Runnable() {

					@Override
					public void run() {
						icon.onClick(p, type);

					}
				});
			}
		}

	}
	
	public ViewPlayer addViewer(Player p) {
		if (views.size() == 0) {
			startUpdateTask();
		}
		ViewPlayer v = new ViewPlayer(this, p);
		views.put(p, v);
		return v;
	}
	
	public void removeViewer(Player p) {
		views.remove(p);
		if (views.size() == 0) {
			stopUpdateTask();
		}
	}

	protected void stopUpdateTask() {
		BukkitTask updateTask = this.updateTask;
		if (updateTask != null && !updateTask.isCancelled()) {
			updateTask.cancel();
		}
	}

	protected void startUpdateTask() {
		stopUpdateTask();
		this.updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(VirtualMenuPlugin.getInstance(), new Runnable() {

			@Override
			public void run() {
				for(ViewPlayer v : getViewPlayers()) {
					for(ExtendedIcon icon : dynamic) {
						ItemStack replace =  v.getItem(icon.getSlot());
						PacketManager.getInstance().updateItem(v, icon.getSlot(), replace);
					}
				}
			}}, 0, period);
		
	}
	
	public Set<Player> getViewers() {
		return views.keySet();
	}
	
	public Collection<ViewPlayer> getViewPlayers() {
		return views.values();
	}

	@Override
	public void apply() {
		ArrayList<ExtendedIcon> tmp_dynamic = new ArrayList<>();
		for(ExtendedIcon icon : icons.values()) {
			if (icon.isPlaceholderapi()) {
				tmp_dynamic.add(icon);
			}
		}
		dynamic.addAll(tmp_dynamic);
	}

	@Override
	public String getLogPrefix() {
		return ILog.sub(name, "menu-settings");
	}

}
