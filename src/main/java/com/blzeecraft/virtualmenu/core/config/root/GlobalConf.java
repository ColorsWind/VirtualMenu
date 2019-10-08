package com.blzeecraft.virtualmenu.core.config.root;

import com.blzeecraft.virtualmenu.core.menu.IMenuType;

import lombok.Data;

@Data
public class GlobalConf {
	
	protected final int refresh;
	protected final String title;
	protected final IMenuType type;

}
