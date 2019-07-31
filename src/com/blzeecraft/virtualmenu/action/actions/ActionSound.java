package com.blzeecraft.virtualmenu.action.actions;

import java.util.StringTokenizer;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;

import lombok.ToString;

@ToString
public class ActionSound extends AbstractAction {
	protected Sound sound = null;
	protected float volume = 1F;
	protected float pitch = 0F;

	public ActionSound(ILog parent, String raw) {
		super(parent, raw, ActionType.SOUND);
		StringTokenizer str = new StringTokenizer(raw, ",");
		String sound = str.nextToken();
		try {
			this.sound = Sound.valueOf(sound);
		} catch (IllegalArgumentException e) {
			PluginLogger.warning(this, "Expect: [sound](,volume,pitch) Set: " + raw + " (INVALID SOUND)");
		}
		if (str.hasMoreTokens()) {
			String volume = str.nextToken();
			try {
				this.volume = Float.parseFloat(volume);
			} catch (NumberFormatException e) {
				PluginLogger.warning(this, "Expect: [sound](,volume,pitch) Set: " + raw + " (volume ISN\'t float)");
			}
		}
		if (str.hasMoreTokens()) {
			String pitch = str.nextToken();
			try {
				this.pitch = Float.parseFloat(pitch);
			} catch (NumberFormatException e) {
				PluginLogger.warning(this, "Expect: [sound](,volume,pitch) Set: " + raw + " (pitch ISN\'t float)");
			}
		}
	}

	@Override
	public void execute(Player p) {
		p.playSound(p.getLocation(), sound, volume, pitch);
	}
	
	

}
