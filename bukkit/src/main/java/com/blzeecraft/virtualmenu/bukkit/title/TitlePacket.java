package com.blzeecraft.virtualmenu.bukkit.title;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;

public class TitlePacket implements ITitleAPI {
	
	public static ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	

	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		try {
			PacketContainer packet = manager.createPacket(Server.TITLE);
			packet.getTitleActions().write(0, TitleAction.TITLE);
			packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
			StructureModifier<Integer> modify = packet.getIntegers();
			modify.write(0, fadeIn);
			modify.write(1, stay);
			modify.write(2, fadeOut);
			if (subtitle == null) {
				try {
					manager.sendServerPacket(player, packet);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else {
				PacketContainer packetSub = manager.createPacket(Server.TITLE);
				packetSub.getTitleActions().write(0, TitleAction.SUBTITLE);
				packetSub.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));
				StructureModifier<Integer> modifySub = packet.getIntegers();
				modifySub.write(0, fadeIn);
				modifySub.write(1, stay);
				modifySub.write(2, fadeOut);
				try {
					manager.sendServerPacket(player, packet);
					manager.sendServerPacket(player, packetSub);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} 
		} catch (IllegalArgumentException e) {
			player.sendMessage(title + "|" + subtitle);
		}
		
	}

	@Override
	public void sendActionBar(Player p, String msg) {
		PacketContainer packet = manager
				.createPacket(Server.CHAT);
		packet.getChatComponents().write(0, WrappedChatComponent.fromText(msg));
		packet.getBytes().write(0, (byte) 2);
		try {
			manager.sendServerPacket(p, packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendHeaderAndFooter(Player p, String head, String foot) {
		PacketContainer packet = manager
				.createPacket(Server.PLAYER_LIST_HEADER_FOOTER);
		StructureModifier<WrappedChatComponent> modify = packet.getChatComponents();
		modify.write(0, WrappedChatComponent.fromText(head));
		modify.write(2, WrappedChatComponent.fromText(foot));
		try {
			manager.sendServerPacket(p, packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}

}
