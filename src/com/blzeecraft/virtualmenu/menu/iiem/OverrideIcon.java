package com.blzeecraft.virtualmenu.menu.iiem;

import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.Node;
import com.blzeecraft.virtualmenu.logger.ILog;
import com.blzeecraft.virtualmenu.menu.ChestMenu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OverrideIcon extends Icon implements Comparable<OverrideIcon> {

	@Node(key = "PRIORITY", type = DataType.INT)
	protected int priority;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected final ExtendedIcon parent;
	
	public OverrideIcon(ChestMenu menu, ExtendedIcon parent) {
		super(menu);
		this.parent = parent;
	}
	
	@Override
	public int compareTo(OverrideIcon o) {
		return Integer.compareUnsigned(priority, o.priority);
	}
	
	@Override
	public String getLogPrefix() {
		return ILog.sub(menu.getLogPrefix(), parent.name, "OVERRIDE");
	}

}
