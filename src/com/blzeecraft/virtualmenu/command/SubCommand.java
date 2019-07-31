package com.blzeecraft.virtualmenu.command;

import com.blzeecraft.virtualmenu.VirtualMenuPlugin;
import com.blzeecraft.virtualmenu.command.annotation.Arg;
import com.blzeecraft.virtualmenu.command.annotation.Args;
import com.blzeecraft.virtualmenu.command.annotation.Help;
import com.blzeecraft.virtualmenu.command.annotation.Name;

import lombok.Getter;

public abstract class SubCommand {
	
	protected final VirtualMenuPlugin pl;
	protected Arg[] requireArgs;
	protected String[] name;
	protected String help;
	
	public SubCommand(VirtualMenuPlugin pl) {
		this.pl = pl;
		Class<? extends SubCommand> clazz = this.getClass();
		requireArgs = clazz.getDeclaredAnnotation(Args.class).value();
		name = clazz.getDeclaredAnnotation(Name.class).value();
		help = clazz.getDeclaredAnnotation(Help.class).value();
	}

	@Getter
	public class ConvertArgs {
		
		private int errorIndex = -1;
		private Arg errorConverter;
		private String errorArg;
		private String errorMessage;
		private Object[] convertArgs;

		public ConvertArgs(String[] args) {
			int length = args.length;
			convertArgs = new Object[length];
			for(int i=0;i<length;i++) {
				String arg = args[i];
				Object o = requireArgs[i].convert(arg);
				if (o == null) {
					errorIndex = i;
					errorConverter = requireArgs[i];
					errorArg = arg;
					errorMessage = errorConverter.getErrorMessage(arg);
					break;
				} else {
					convertArgs[i] = o;
				}
			}
		}
		

	}
}
