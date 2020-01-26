package com.blzeecraft.virtualmenu.core.conf;

import java.util.List;
import java.util.Map;

import lombok.ToString;

/**
 * 表示文件读取得到的数据,是 {@link File} 到 {@link PacketMenu} 的中间类型}}
 * 该类型用于储存文件中未解析的结构化数据
 * @author colors_wind
 *
 */
@ToString
public class StandardMenuFile extends SubSection {
	
	public GlobalConf global;
	@ToString public class GlobalConf extends SubSection {
		public String title;
		public String type;
		public int refresh;
		public List<String> bound;
	}
	
	public Map<String, EventConf> events;
	@ToString public class EventConf extends SubSection {
		public List<String> condtion;
		public List<String> action;
	}
	
	public Map<String, IconConf> icons;
	@ToString public class IconConf extends SubSection {
		public String name;
		public String id;
		public String nbt;
		public List<String> lore;
		public List<String> action;
		public List<String> view_condition;
		public List<String> click_condition;
		public int priority;
		public int postion_x;
		public int postion_y;
		public int slot;
		
	}
	
}