package com.blzeecraft.virtualmenu.core.conf;

public interface StringSerializable {
	
	String serialize();
	
	default String[] seriablizeAll() {
		String s = serialize();
		if (s == null) {
			return new String[0];
		}
		return new String[] {s};
	}
}
