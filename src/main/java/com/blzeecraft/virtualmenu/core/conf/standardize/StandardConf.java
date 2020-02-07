package com.blzeecraft.virtualmenu.core.conf.standardize;

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
public class StandardConf extends SubConf {
	
	public GlobalConf global;
	@ToString public static class GlobalConf extends SubConf {
		public String title;
		public String type;
		public int refresh;
		public List<String> bound;
	}
	
	public Map<String, EventConf> events;
	@ToString public static class EventConf extends SubConf {
		public List<String> condtion;
		public List<String> action;
	}
	
	public Map<String, IconConf> icons;
	@ToString public static class IconConf extends SubConf {
		public String name;
		public String id;
		public int amount;
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
