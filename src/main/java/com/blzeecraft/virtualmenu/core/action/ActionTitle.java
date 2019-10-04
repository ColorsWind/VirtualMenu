package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.ToString;

@ToString
public class ActionTitle extends Action {
	protected final String title;
	protected final String subtitle;
	protected final int fadeIn;
	protected final int stay;
	protected final int fadeOut;
	
	public ActionTitle(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.title = rlc.getAsString("title");
		this.subtitle = rlc.getAsOptString("subtitle").orElse("");
		this.fadeIn = rlc.getAsOptInt("fadeIn").orElse(3);
		this.stay = rlc.getAsOptInt("stay").orElse(7);
		this.fadeOut = rlc.getAsOptInt("fadeOut").orElse(3);
	}

	@Override
	public void execute(IUser<?> user) {
		user.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
	}

	
	

}
