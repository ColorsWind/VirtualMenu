package com.blzeecraft.virtualmenu.core.config.root;

import java.util.Map;

import com.blzeecraft.virtualmenu.core.config.map.EventTemplate;
import com.blzeecraft.virtualmenu.core.config.map.IconTemplate;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;
import com.blzeecraft.virtualmenu.core.menu.EventType;

public class IconFile {
	
	public GlobalConf globalConf;
	
	@EnumNode(key = EventType.class, template = EventTemplate.class)
	public Map<EventType, EventHandler> eventHandlers;
	
	@EnumNode(key = null, template = IconTemplate.class)
	public Map<String, Icon> icons;

}
