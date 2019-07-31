package com.blzeecraft.virtualmenu.action;

import org.bukkit.entity.Player;

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

}
