package com.blzeecraft.virtualmenu.bukkit.packet;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.blzeecraft.virtualmenu.bukkit.MenuType;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class PacketPlayOutWindowOpen1_7 extends AbstractPacketOutWindowOpen<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换

	public PacketPlayOutWindowOpen1_7() {
		super(new PacketContainer(PacketType.Play.Server.OPEN_WINDOW));
	}

	@Override
	public void setTitle(String title) {
		packet.getStrings().write(0, title);
	}

	@Override
	public String getTitle() {
		return packet.getStrings().read(0);
	}

	@Override
	public void setMenuType(IMenuType menuType) {
		packet.getIntegers().write(1, Math.max(0, menuType.getLegacyIndex()));
		packet.getIntegers().write(2, menuType.getSize());

	}

	@Override
	public IMenuType getMenuType() throws NoSuchElementException {
		int legacyIndex = packet.getIntegers().read(1);
		int size = packet.getIntegers().read(2);
		return Arrays.stream(MenuType.values()).filter(type -> type.getLegacyIndex() == legacyIndex)
				.filter(type -> type.getSize() == size).findAny().get();
	}

	@Override
	public void setWindowId(int windowId) {
		packet.getSpecificModifier(boolean.class).write(0, true);
		packet.getIntegers().write(0, windowId);
	}

	@Override
	public int getWindowId() {
		return packet.getIntegers().read(0);
	}

}
