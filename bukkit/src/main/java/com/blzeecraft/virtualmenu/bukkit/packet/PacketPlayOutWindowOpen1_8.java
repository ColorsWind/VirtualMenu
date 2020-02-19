package com.blzeecraft.virtualmenu.bukkit.packet;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.blzeecraft.virtualmenu.bukkit.MenuType;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PacketPlayOutWindowOpen1_8 extends AbstractPacketOutWindowOpen<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换

	public PacketPlayOutWindowOpen1_8() {
		super(new PacketContainer(PacketType.Play.Server.OPEN_WINDOW));
	}

	@Override
	public void setTitle(String title) {
		WrappedChatComponent component = WrappedChatComponent.fromText(title);
		packet.getChatComponents().write(0, component);
	}

	@Override
	public String getTitle() {
		return packet.getChatComponents().read(0).toString();
	}

	@Override
	public void setMenuType(IMenuType menuType) {
		packet.getStrings().write(0, menuType.getMinecraftKey());
		packet.getIntegers().write(1, menuType.getSize());
		
	}

	@Override
	public IMenuType getMenuType() throws NoSuchElementException {
		String minecraftKey = packet.getStrings().read(0);
		int size = packet.getIntegers().read(1);
		return Arrays.stream(MenuType.values()).filter(type -> type.getMinecraftKey().equalsIgnoreCase(minecraftKey)).filter(type -> type.getSize() == size).findAny().get();
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
