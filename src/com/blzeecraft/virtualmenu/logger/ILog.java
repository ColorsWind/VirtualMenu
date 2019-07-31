package com.blzeecraft.virtualmenu.logger;

public interface ILog {
	public static final String END = ": ";
	public static final String SPRIT = ">";
	
	public static String sub(String parent, String... sub) {
		StringBuilder sb = new StringBuilder(parent);
		for(int i=0;i<sub.length;i++) {
			if (sub[i] != null) {
				sb.append(SPRIT);
				sb.append(sub[i]);
			}
		}
		return sb.toString();
	}
	
	String getLogPrefix();
}
