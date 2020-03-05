package com.blzeecraft.virtualmenu.core.action.extension;

import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;

@ToString
public class ActionSound extends Action {
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


	@Override
	public String getKey() {
		return "sound";
	}
	
	

}
