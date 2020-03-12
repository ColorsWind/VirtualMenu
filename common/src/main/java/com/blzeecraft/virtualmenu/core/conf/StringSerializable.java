package com.blzeecraft.virtualmenu.core.conf;

/**
 * 代表这个类可以被序列化成 {@link String}.
 * @author colors_wind
 *
 */
public interface StringSerializable {
	
	String serialize();
	
	default String[] seriablizeAll() {
		return new String[] {serialize()};
	}
}
