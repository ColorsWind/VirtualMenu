package com.blzeecraft.virtualmenu.bukkit.packet;

import com.blzeecraft.virtualmenu.bukkit.MenuType;
import com.blzeecraft.virtualmenu.core.menu.IMenuType;
import com.blzeecraft.virtualmenu.core.packet.AbstractPacketOutWindowOpen;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class PacketPlayOutWIndowOpen1_14  extends AbstractPacketOutWindowOpen<PacketContainer> {
	protected final PacketContainer packet = super.packet; // 避免强制准换

	public PacketPlayOutWIndowOpen1_14() {
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
		packet.getIntegers().write(1, menuType.getIndex());
	}

	@Override
	public IMenuType getMenuType() {
		int index = packet.getIntegers().read(1);
		return MenuType.values()[index];
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
