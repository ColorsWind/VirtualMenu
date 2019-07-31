package com.blzeecraft.virtualmenu.packet;

import java.lang.reflect.InvocationTargetException;

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
			if (windowId == menu.getID() && slot <= menu.getSlots()) {
				int button = packet.getIntegers().read(2);
				ClickMode mode = packet.getEnumModifier(ClickMode.class, 5).read(0);
				ItemStack clickedItem = packet.getItemModifier().read(0);
				ClickType type = getClickType(mode, button);
				e.setReadOnly(false);
				e.setCancelled(true);
				ItemStack[] items = manager.cacheItems.get(p);
				ItemStack item = null;
				if (items == null) {
					item = clickedItem;
				} else {
					item = items[slot];
				}
				PacketSetSlot keep = new PacketSetSlot(menu.getID(), slot, item);
				try {
					keep.send(p);
				} catch (InvocationTargetException ec) {
					ec.printStackTrace();
				}
				PacketSetSlot reset = new PacketSetSlot(-1, -1, new ItemStack(Material.AIR));
				try {
					reset.send(p);
				} catch (InvocationTargetException ex) {
					ex.printStackTrace();
				}
				menu.click(slot, p, type, clickedItem);
			}
		}
	}
	
	public ClickType getClickType(ClickMode mode, int button){
		if(mode == ClickMode.PICKUP){
			if(button == 0)
			{
				return ClickType.LEFT;
			}else{
				return ClickType.RIGHT;
			}
		}else if(mode == ClickMode.QUICK_MOVE){
			if(button == 0){
				return ClickType.SHIFT_LEFT;
			}else{
				return ClickType.SHIFT_RIGHT;
			}
		}else if(mode == ClickMode.SWAP){
			return ClickType.NUMBER_KEY;
		}else if(mode == ClickMode.CLONE && button == 2){
			return ClickType.MIDDLE;
		}else if(mode == ClickMode.THROW){
			if(button == 0){
				return ClickType.DROP;
			}else if(button == 1){
				return ClickType.CONTROL_DROP;
			}
		}else if(mode == ClickMode.PICKUP_ALL){
			return ClickType.DOUBLE_CLICK;
		}else if(mode == ClickMode.QUICK_CRAFT){
			return null;
		}
		return ClickType.UNKNOWN;
			
	}

}
