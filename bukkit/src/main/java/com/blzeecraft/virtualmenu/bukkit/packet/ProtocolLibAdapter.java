package com.blzeecraft.virtualmenu.bukkit.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bukkit.item.XMaterial;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutCloseWindow;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutSetSlot;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowItems;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.blzeecraft.virtualmenu.core.packet.AbstractWindowPacket;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.user.IUser;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

public class ProtocolLibAdapter implements IPacketAdapter {
	public static final int VERSION = XMaterial.getVersion();
	public static final Supplier<AbstractPacketOutCloseWindow<?>> SUPPLIER_CLOSE_WINDOW;
	public static final Supplier<AbstractPacketOutWindowOpen<?>> SUPPLIER_WINDOW_OPEN;
	public static final Supplier<AbstractPacketOutSetSlot<?>> SUPPLIER_SET_SLOT;
	public static final Supplier<AbstractPacketOutWindowItems<?>> SUPPLIER_WINDOW_ITEMS;
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
		} else {
			SUPPLIER_WINDOW_ITEMS = PacketPlayOutWindowItems1_7::new;
		}
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

	@Override
	public void sendServerPacket(IUser<?> user, AbstractWindowPacket<?> packet) throws InvocationTargetException {
		ProtocolLibrary.getProtocolManager().sendServerPacket((Player) user.getHandle(),
				(PacketContainer) packet.getHandle());
	}
	

}
