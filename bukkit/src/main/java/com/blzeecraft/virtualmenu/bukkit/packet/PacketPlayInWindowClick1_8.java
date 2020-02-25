package com.blzeecraft.virtualmenu.bukkit.packet;

import org.bukkit.inventory.ItemStack;

import com.blzeecraft.virtualmenu.bukkit.WrapItemBukkit;
import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketInWindowClick;
import com.blzeecraft.virtualmenu.core.packet.ClickMode;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayInWindowClick1_8 extends AbstractPacketInWindowClick<PacketContainer> {

	public PacketPlayInWindowClick1_8(PacketContainer handle) {
		super(handle);
	}

	@Override
	public void setRawSlot(int slot) {
		packet.getIntegers().write(1, slot);
	}

	@Override
	public int getRawSlot() {
		return packet.getIntegers().read(1);
	}

	@Override
	public void setClickMode(ClickMode mode) {
		packet.getEnumModifier(ClickMode.class, 5).write(0, mode);
	}

	@Override
	public ClickMode getClickMode() {
		return packet.getEnumModifier(ClickMode.class, 5).read(0);
	}

	@Override
	public void setButton(int button) {
		packet.getIntegers().write(2, button);
	}

	@Override
	public int getButton() {
		return packet.getIntegers().read(2);
	}

	@Override
	public void setClickedItem(AbstractItem<?> item) {
		ItemStack clickedItem = (ItemStack) item.getHandle();
		packet.getItemModifier().write(0, clickedItem);
	}

	@Override
	public AbstractItem<?> getClickedItem() {
		ItemStack clickedItem = packet.getItemModifier().read(0);
		return new WrapItemBukkit(clickedItem);
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getIntegers().write(0, windowId);
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

}
