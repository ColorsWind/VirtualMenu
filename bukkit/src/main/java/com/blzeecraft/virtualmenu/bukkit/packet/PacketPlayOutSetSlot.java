package com.blzeecraft.virtualmenu.bukkit.packet;

import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.WrapItemBukkit;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutSetSlot;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutSetSlot extends AbstractPacketOutSetSlot<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换
	
	public PacketPlayOutSetSlot() {
		this(new PacketContainer(PacketType.Play.Server.SET_SLOT));
	}
	
	public PacketPlayOutSetSlot(PacketContainer packet) {
		super(packet);
	}

	@Override
	public void setSlot(int slot) {
		packet.getIntegers().write(1, slot);
	}

	@Override
	public int getSlot() {
		return packet.getIntegers().read(1);
	}

	@Override
	public void setItem(AbstractItem<?> item) {
		this.setRawItem(item.getHandle());
	}

	@Override
	public AbstractItem<?> getItem() {
		return new WrapItemBukkit(packet.getItemModifier().read(0));
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getIntegers().write(0, windowId);
		
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

	@Override
	public void setRawItem(Object item) {
		packet.getItemModifier().write(0, (ItemStack) item);
	}

	@Override
	public ItemStack getRawItem() {
		return packet.getItemModifier().read(0);
	}

}
