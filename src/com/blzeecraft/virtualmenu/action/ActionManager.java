package com.blzeecraft.virtualmenu.action;

import java.util.StringTokenizer;

import com.blzeecraft.virtualmenu.InsensitiveMap;
import com.blzeecraft.virtualmenu.action.actions.ActionActionbar;
import com.blzeecraft.virtualmenu.action.actions.ActionCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionConsoleCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionOpCommand;
import com.blzeecraft.virtualmenu.action.actions.ActionSound;
import com.blzeecraft.virtualmenu.action.actions.ActionTell;
import com.blzeecraft.virtualmenu.action.actions.ActionTitle;
import com.blzeecraft.virtualmenu.logger.ILog;

import lombok.Getter;

public class ActionManager {

	public static final ActionExecutor<ActionSound> SOUND = new ActionExecutor<ActionSound>() {

		@Override
		public ActionSound fromString(ILog il, String raw) {
			return new ActionSound(il, raw);
		}
	};
	
	public static final ActionExecutor<ActionActionbar> ACTIONBAR = new ActionExecutor<ActionActionbar>() {

		@Override
		public ActionActionbar fromString(ILog il, String raw) {
			return new ActionActionbar(il, raw);
		}
	};
	public static final ActionExecutor<ActionTitle> TITLE = new ActionExecutor<ActionTitle>() {

		@Override
		public ActionTitle fromString(ILog il, String raw) {
			return new ActionTitle(il, raw);
		}
	};
	public static final ActionExecutor<ActionTell> TELL = new ActionExecutor<ActionTell>() {

		@Override
		public ActionTell fromString(ILog il, String raw) {
			return new ActionTell(il, raw);
		}
	};
	public static final ActionExecutor<ActionCommand> COMMAND = new ActionExecutor<ActionCommand>() {

		@Override
		public ActionCommand fromString(ILog il, String raw) {
			return new ActionCommand(il, raw);
		}
	};
	public static final ActionExecutor<ActionOpCommand> OP_COMMAND = new ActionExecutor<ActionOpCommand>() {

		@Override
		public ActionOpCommand fromString(ILog il, String raw) {
			return new ActionOpCommand(il, raw);
		}
	};
	public static final ActionExecutor<ActionConsoleCommand> CONSOLE_COMMAND = new ActionExecutor<ActionConsoleCommand>() {
		@Override
		public ActionConsoleCommand fromString(ILog il, String raw) {
			return new ActionConsoleCommand(il, raw);
		}
	};
	
	@Getter
	protected static ActionManager instance;
	
	public static ActionManager init() {
		return instance = new ActionManager();
	}
	
	protected InsensitiveMap<ActionExecutor<?>> executors = new InsensitiveMap<>();
	
	public AbstractAction fromString(ILog parent, String s) {
		StringTokenizer str = new StringTokenizer(s, ":");
		String prefix = str.nextToken();
		if (str.hasMoreTokens()) {
			String raw = str.nextToken();
			ActionExecutor<?> executor = executors.get(prefix);
			if (executor != null) {
				return executor.fromString(parent, raw);
			} 
		}
		return COMMAND.fromString(parent, s);
	}

	public void registerActions() {
		executors.put("", COMMAND);
		executors.put("op", OP_COMMAND);
		executors.put("console", CONSOLE_COMMAND);
		executors.put("tell", TELL);
		executors.put("actionbar", ACTIONBAR);
		executors.put("title", TITLE);
		executors.put("sound", SOUND);
	}

	

}
