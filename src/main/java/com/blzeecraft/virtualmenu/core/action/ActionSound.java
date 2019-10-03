package com.blzeecraft.virtualmenu.core.action;

import com.blzeecraft.virtualmenu.core.IUser;
import com.blzeecraft.virtualmenu.core.config.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.ToString;

@ToString
public class ActionSound extends AbstractAction {
	protected final String sound;
	protected final float volume;
	protected final float pitch;

	public ActionSound(LogNode node, ResolvedLineConfig rlc) {
		super(node, rlc);
		this.sound = rlc.getAsString("sound");
		this.volume = (float) rlc.getAsOptDouble("volume").orElse(1D);
		this.pitch = (float) rlc.getAsOptDouble("pitch").orElse(0D);
	}


	@Override
	public void execute(IUser<?> user) {
		user.playSound(sound, volume, pitch);
		
	}
	
	

}
