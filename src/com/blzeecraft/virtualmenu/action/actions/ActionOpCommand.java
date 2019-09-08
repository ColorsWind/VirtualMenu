package com.blzeecraft.virtualmenu.action.actions;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;

public class ActionOpCommand extends AbstractAction {

	public ActionOpCommand(ILog parent, String raw) {
		super(parent, raw, ActionType.OP_COMMAND);
		PluginLogger.warning(this, "op:存在安全隐患,请尽量使用console:代替");
	}

	@Override
	public void execute(Player p) {
		//危险操作
		if (p.isOp()) {
			this.execute(p, raw.replace("<player>", p.getName()));
		} else {
			try {
				p.setOp(true);
				this.execute(p, raw.replace("<player>", p.getName()));
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				p.setOp(false);
			}
		}
	}

}
