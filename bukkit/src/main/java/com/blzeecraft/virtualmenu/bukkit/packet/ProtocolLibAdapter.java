package com.blzeecraft.virtualmenu.bukkit.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bukkit.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.bukkit.item.XMaterial;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInWindowClick;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutSetSlot;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.blzeecraft.virtualmenu.core.packet.AbstractWindowPacket;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class ProtocolLibAdapter implements IPacketAdapter {
	public static final int VERSION = XMaterial.getVersion();
	public static final Supplier<AbstractPacketOutCloseWindow<?>> SUPPLIER_CLOSE_WINDOW;
	public static final Supplier<AbstractPacketOutWindowOpen<?>> SUPPLIER_WINDOW_OPEN;
	public static final Supplier<AbstractPacketOutSetSlot<?>> SUPPLIER_SET_SLOT;
	public static final Supplier<AbstractPacketOutWindowItems<?>> SUPPLIER_WINDOW_ITEMS;
	public static final Function<PacketContainer, AbstractPacketInWindowClick<?>> FUNCTION_WINDOW_CLICK;
	public static final Function<PacketContainer, AbstractPacketInCloseWindow<?>> FUNCTION_CLOSE_WINDOW;
	public static final Function<PacketContainer, AbstractPacketOutSetSlot<?>> FUNCTION_SET_SLOT;
	public static final Function<PacketContainer, AbstractPacketOutWindowItems<?>> FUNCTION_WINDOW_ITEMS;
	static {
		SUPPLIER_CLOSE_WINDOW = PacketPlayOutCloseWindow::new;
		SUPPLIER_SET_SLOT = PacketPlayOutSetSlot::new;
		if (VERSION >= 14) {
			SUPPLIER_WINDOW_OPEN = PacketPlayOutWIndowOpen1_14::new;
		} else if (VERSION > 8) {
			SUPPLIER_WINDOW_OPEN = PacketPlayOutWindowOpen1_8::new;
		} else {
			SUPPLIER_WINDOW_OPEN = PacketPlayOutWindowOpen1_7::new;
		}
		if (VERSION >= 11) {
			SUPPLIER_WINDOW_ITEMS = PacketPlayOutWindowItems1_11::new;
			FUNCTION_WINDOW_ITEMS = PacketPlayOutWindowItems1_11::new;
		} else {
			SUPPLIER_WINDOW_ITEMS = PacketPlayOutWindowItems1_7::new;
			FUNCTION_WINDOW_ITEMS = PacketPlayOutWindowItems1_7::new;
		}
		if (VERSION >= 8) {
			FUNCTION_WINDOW_CLICK = PacketPlayInWindowClick1_8::new;
		} else {
			FUNCTION_WINDOW_CLICK = PacketPlayInWindowClick1_7::new;
		}
		FUNCTION_CLOSE_WINDOW = PacketPlayInCloseWindow::new;
		FUNCTION_SET_SLOT = PacketPlayOutSetSlot::new;
		
	}

	private final VirtualMenuPlugin plugin;
	private final ProtocolManager protocolManager;
	private final PacketListener packetListener;


	public ProtocolLibAdapter(VirtualMenuPlugin plugin) {
		this.plugin = plugin;
		this.protocolManager = ProtocolLibrary.getProtocolManager();
		//plugin packetAdapter 尚未初始化, 必须传递
		this.packetListener = new PacketListener(plugin, this);
	}

	@Override
	public AbstractPacketOutCloseWindow<?> createPacketCloseWindow() {
		return SUPPLIER_CLOSE_WINDOW.get();
	}

	@Override
	public AbstractPacketOutWindowOpen<?> createPacketWindowOpen() {
		return SUPPLIER_WINDOW_OPEN.get();
	}

	@Override
	public AbstractPacketOutSetSlot<?> createPacketSetSlot() {
		return SUPPLIER_SET_SLOT.get();
	}

	@Override
	public AbstractPacketOutWindowItems<?> createPacketWindowItems() {
		return SUPPLIER_WINDOW_ITEMS.get();
	}
	
	public AbstractPacketInWindowClick<?> mapToWindowClick(PacketContainer packet) {
		return FUNCTION_WINDOW_CLICK.apply(packet);
	}
	
	public AbstractPacketInCloseWindow<?> mapToCloseWindow(PacketContainer packet) {
		return FUNCTION_CLOSE_WINDOW.apply(packet);
	}
	
	public AbstractPacketOutSetSlot<?> mapToSetSlot(PacketContainer packet) {
		return FUNCTION_SET_SLOT.apply(packet);
	}
	
	public AbstractPacketOutWindowItems<?> mapToWindowItems(PacketContainer packet) {
		return FUNCTION_WINDOW_ITEMS.apply(packet);
	}


	@Override
	public void sendServerPacket(IUser<?> user, AbstractWindowPacket<?> packet) throws InvocationTargetException {	 
		protocolManager.sendServerPacket((Player) user.getHandle(),
				(PacketContainer) packet.getHandle());
	}

	@Override
	public String getVersion() {
		return "ProtocolLib PacketAdapter";
	}

	public void registerEvent() {
		protocolManager.addPacketListener(packetListener);
		Bukkit.getPluginManager().registerEvents(packetListener, plugin);
		// for debug
		protocolManager.addPacketListener(new PacketDebugHandler(plugin));

	}

}
