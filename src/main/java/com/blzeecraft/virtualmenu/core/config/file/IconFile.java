package com.blzeecraft.virtualmenu.core.config.file;

import java.util.Map;

import com.blzeecraft.virtualmenu.core.config.template.EventTemplate;
import com.blzeecraft.virtualmenu.core.config.template.GlobalConfTemplate;
import com.blzeecraft.virtualmenu.core.config.template.GlobalConfTemplate.GlobalConf;
import com.blzeecraft.virtualmenu.core.config.template.IconTemplate;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;
import com.blzeecraft.virtualmenu.core.menu.EventType;

public class IconFile {
	
	@SpecificNode(key = "global", template = GlobalConfTemplate.class)
	public GlobalConf globalConf;
	
	@EnumNode(key = EventType.class, template = EventTemplate.class)
	public Map<EventType, EventHandler> eventHandlers;
	
	@ArbitrarilyNode( template = IconTemplate.class)
	public Map<String, Icon> icons;

}
