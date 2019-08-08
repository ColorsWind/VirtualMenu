package com.blzeecraft.virtualmenu.packet;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.menu.ChestMenu;
import com.blzeecraft.virtualmenu.packet.packets.PacketSetSlot;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class PacketWindowClickHandler  extends PacketAdapter {
	private final PacketManager manager;

	public PacketWindowClickHandler(VirtualMenuPlugin pl, PacketManager manager) {
		super(pl, ListenerPriority.MONITOR, 
				PacketType.Play.Client.WINDOW_CLICK);
		this.manager = manager;
	}
	
	@Override
	public void onPacketReceiving(PacketEvent e) {
		Player p = e.getPlayer();
		ChestMenu menu = manager.openMenus.get(p);
		if (menu != null) {
			PacketContainer packet = e.getPacket();
			int windowId = packet.getIntegers().read(0);
			int slot = packet.getIntegers().read(1);
			System.out.println(slot);
			//不处理点击PlayerInventory的数据包
			//若点击了PlayerInventory的物品，那么slot > menu.slot
			if (windowId == menu.getID() && slot <= menu.getSlots()) {
				//预处理开始
				int button = packet.getIntegers().read(2);
				ClickMode mode = packet.getEnumModifier(ClickMode.class, 5).read(0);
				ItemStack clickedItem = packet.getItemModifier().read(0);
				ClickType type = getClickType(mode, button, slot);
				e.setReadOnly(false);
				e.setCancelled(true);
				

				
				//重设玩家背包显示
				ItemStack[] items = manager.cacheItems.get(p);
				ItemStack item = null;
				if (items == null) {
					item = clickedItem;
				} else {
					item = items[slot];
				}
				PacketSetSlot keep = new PacketSetSlot(menu.getID(), slot, item);
				PacketSetSlot reset = new PacketSetSlot(-1, -1, new ItemStack(Material.AIR));
				if(e.isAsync()) {
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							try {
								keep.send(p);
								reset.send(p);
							} catch (InvocationTargetException ec) {
								ec.printStackTrace();
							}
							if(mode == ClickMode.QUICK_MOVE) {
								p.updateInventory();
							}
						}});
				} else {
					try {
						keep.send(p);
						reset.send(p);
					} catch (InvocationTargetException ec) {
						ec.printStackTrace();
					}
					if(mode == ClickMode.QUICK_MOVE) {
						p.updateInventory();
					}
				}
				
				//执行命令
				menu.click(slot, p, type, clickedItem);
			}
		}
	}
	
	public ClickType getClickType(ClickMode mode, int button, int slot){
		switch(mode) {
		case PICKUP: 
			if (button == 0) {
				return ClickType.LEFT;
			} else if (button == 1) {
				return ClickType.RIGHT;
			}
			break;
		case QUICK_MOVE:
			if (button == 0) {
				return ClickType.SHIFT_LEFT;
			} else if (button == 1) {
				return ClickType.SHIFT_RIGHT;
			}
			break;
		case SWAP:
			return ClickType.NUMBER_KEY;
		case CLONE:
			return ClickType.MIDDLE;
		case THROW:
			if (slot >= 0) {
				if (button == 0) {
					return ClickType.DROP;
				} else if (button == 1) {
					return ClickType.CONTROL_DROP;
				}
			} else if (slot == -999) {
				if (button == 0) {
					return ClickType.WINDOW_BORDER_LEFT;
				} else if (button == 1) {
					return ClickType.WINDOW_BORDER_RIGHT;
				}
			}
			break;
		case QUICK_CRAFT:
			if (slot >= 0) {
				if (button == 1) {
					return ClickType.LEFT;
				} else if (button == 5) {
					return ClickType.RIGHT;
				}
			} else if (slot == -999) {
				switch(button) {
				case 0:
				case 1:
				case 2:
					return ClickType.LEFT;
				case 4:
				case 5:
				case 6:
					return ClickType.RIGHT;
				case 8:
				case 9:
				case 10:
					return ClickType.MIDDLE;
				}
			}
		case PICKUP_ALL:
			return ClickType.DOUBLE_CLICK;
		}
		return ClickType.UNKNOWN;
	}

}
