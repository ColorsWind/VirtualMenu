package com.blzeecraft.virtualmenu.core.conf;

import com.blzeecraft.virtualmenu.core.conf.standardize.SubConf;

public interface ConfSerializable<T extends SubConf> {
	
	T serialize();
	
	T[] seriablizeAll();
}
