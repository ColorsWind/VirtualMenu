package com.blzeecraft.virtualmenu.core.action.extension;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import com.blzeecraft.virtualmenu.core.action.Action;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.user.IUser;

import lombok.ToString;
import lombok.val;

@ToString
public class ActionSound extends Action {
	public static final String KEY = "sound";
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
		return KEY;
	}

	public static String remap(String value) {
		val map = new LinkedHashMap<String, String>();
		val str = new StringTokenizer(value, ",");
		map.put("sound", str.nextToken());
		if (str.hasMoreElements()) {
			map.put("volume", str.nextToken());
		}
		if (str.hasMoreElements()) {
			map.put("pitch", str.nextToken());
		}
		return KEY + new ResolvedLineConfig(map).toString();
	}

}
