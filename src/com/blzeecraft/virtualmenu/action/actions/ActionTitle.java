package com.blzeecraft.virtualmenu.action.actions;

import java.util.StringTokenizer;

import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.action.AbstractAction;
import com.blzeecraft.virtualmenu.action.ActionType;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.logger.PluginLogger;
import com.blzeecraft.virtualmenu.title.Constants;
import com.blzeecraft.virtualmenu.title.TitleAPI;

import lombok.ToString;

@ToString
public class ActionTitle extends AbstractAction {
	protected String title = null;
	protected String subtitle = null;
	protected int fadeIn = Constants.TITLE_FADE;
	protected int stay = Constants.TITLE_STAY;
	protected int fadeOut = Constants.TITLE_FADE;

	public ActionTitle(ILog parent, String raw) {
		super(parent, raw, ActionType.TITLE);
		StringTokenizer str = new StringTokenizer(raw, ",");
		title = str.nextToken();
		if (str.hasMoreTokens()) {
			subtitle = str.nextToken();
		} else {
			subtitle = "";
		}
		if (str.hasMoreTokens()) {
			String fadeIn = str.nextToken();
			try {
				this.fadeIn = Integer.parseInt(fadeIn);
			} catch (NumberFormatException e) {
				PluginLogger.warning(this, "Expect: [title](,subtitle,fadeIn,stay,fadeOut) Set: " + raw + " (fadeIn ISN\'t int)");
			}
		}
		if (str.hasMoreTokens()) {
			String stay = str.nextToken();
			try {
				this.stay = Integer.parseInt(stay);
			} catch (NumberFormatException e) {
				PluginLogger.warning(this, "Expect: [title](,subtitle,fadeIn,stay,fadeOut) Set: " + raw + " (stay ISN\'t int)");
			}
		}
		if (str.hasMoreTokens()) {
			String fadeOut = str.nextToken();
			try {
				this.fadeOut = Integer.parseInt(fadeOut);
			} catch (NumberFormatException e) {
				PluginLogger.warning(this, "Expect: [title](,subtitle,fadeIn,stay,fadeOut) Set: " + raw + " (fadeOut ISN\'t int)");
			}
		}
	}

	@Override
	public void execute(Player p, boolean isPlaceholderAPI) {
		TitleAPI.sendTitle(p, title, subtitle, fadeIn, stay, fadeOut);
	}
	
	

}
