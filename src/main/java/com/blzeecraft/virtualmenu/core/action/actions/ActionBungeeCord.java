package com.blzeecraft.virtualmenu.core.action.actions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.config.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.ToString;

@ToString
public class ActionBungeeCord extends Action {
	protected final String toServer;


	public ActionBungeeCord(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.toServer = rlc.getAsString("server");
	}

	@Override
	public void execute(IUser<?> user) {
		VirtualMenu.runTaskAsync(() -> {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteArray);
			try {
				out.writeUTF("Connect");
				out.writeUTF(toServer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			user.sendPluginMessage("BungeeCord", byteArray.toByteArray());
		});
	}

}
