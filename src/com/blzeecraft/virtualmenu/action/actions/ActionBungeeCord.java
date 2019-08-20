package com.blzeecraft.virtualmenu.action.actions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;

public class ActionBungeeCord extends AbstractAction {

	public ActionBungeeCord(ILog parent, String raw) {
		super(parent, raw, ActionType.BUNGEECORD);
	}

	@Override
	public void execute(Player p) {
		
		Bukkit.getScheduler().runTaskAsynchronously(VirtualMenuPlugin.getInstance(), () -> {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteArray);
			try {
				out.writeUTF("Connect");
				out.writeUTF(raw);
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.sendPluginMessage(VirtualMenuPlugin.getInstance(), "BungeeCord", byteArray.toByteArray());
		});
	}

}
