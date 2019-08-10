package com.blzeecraft.virtualmenu.action;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.Getter;

@Getter
public abstract class AbstractAction implements ILog {

	protected final String raw;
	protected final  ILog parent;
	private final ActionType type;

	
	public AbstractAction(ILog parent, String raw, ActionType type) {
		this.parent = parent;
		this.raw = raw;
		this.type = type;
	}
	
	public final ActionType getType() {
		return type;
	}
	
	@Override
	public String getLogPrefix() {
		return ILog.sub(parent.getLogPrefix(), "COMMAND");
	}
	

	public abstract void execute(Player p);
	
	protected boolean execute(Player p, String command) {
		PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(p, "/" + 	command);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			p.performCommand(event.getMessage().substring(1));
		}
		return true;
	}

	public static void run(List<AbstractAction> cmds, Player p) {
		try {
			for (AbstractAction cmd : cmds) {
				cmd.execute(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
