package com.blzeecraft.virtualmenu.core.config.file;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.blzeecraft.virtualmenu.core.config.template.EventTemplate;
import com.blzeecraft.virtualmenu.core.config.template.GlobalConfTemplate;
import com.blzeecraft.virtualmenu.core.config.template.ITemplate;
import com.blzeecraft.virtualmenu.core.config.template.GlobalConfTemplate.GlobalConf;
import com.blzeecraft.virtualmenu.core.config.template.SlotIconTemplate;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.MultiIcon;
import com.blzeecraft.virtualmenu.core.icon.SlotIcon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.menu.EventHandler;
import com.blzeecraft.virtualmenu.core.menu.EventType;
import com.blzeecraft.virtualmenu.core.menu.PacketMenu;

public class MenuFile implements ITemplate<PacketMenu> {

	@SpecificNode(key = "global", template = GlobalConfTemplate.class)
	public GlobalConf globalConf;

	@EnumNode(key = EventType.class, template = EventTemplate.class)
	public Map<EventType, EventHandler> eventHandlers;

	@ArbitrarilyNode(template = SlotIconTemplate.class)
	public Map<String, SlotIcon> icons;

	@Override
	public PacketMenu apply(LogNode node) {
		return new PacketMenu(node, globalConf.getRefresh(), globalConf.getTitle(), globalConf.getType(), flatIcons(), eventHandlers);
	}

	
	public Icon[] flatIcons() {
		Icon[] icons = new Icon[globalConf.getType().size()];
		this.icons.values().stream().collect(Collectors.groupingBy(SlotIcon::getSlot))
				.forEach((k, v) -> icons[k] = v.size() == 1 ? v.get(0).getIcon()
						: new MultiIcon(v.stream().map(SlotIcon::getIcon).collect(Collectors.toList())));
		;
		return icons;
	}

}
