package com.blzeecraft.virtualmenu.menu.iiem;

import java.util.TreeSet;

import org.bukkit.entity.Player;

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
public class ExtendedIcon extends Icon {	

	// position
	@Node(key = "POSITION-X", type = DataType.INT)
	protected int x;

	@Node(key = "POSITION-Y", type = DataType.INT)
	protected int y;
	
	@Node(key = "OVERRIDE", type = DataType.OVERRIDE_ICON)
	protected TreeSet<OverrideIcon> overrides;
	
	protected final String name;
	
	public ExtendedIcon(ChestMenu menu, String name) {
		super(menu);
		this.name = name;
	}

	public int getSlot() {
		return (y - 1) * 9 + (x - 1);
	}


	@Override
	public String getLogPrefix() {
		return ILog.sub(menu.getLogPrefix(), name);
	}

	public Icon getIcon(Player p) {
		if (overrides != null) {
			for(OverrideIcon oi : overrides.descendingSet()) {
				if (oi.canView(p)) {
					return oi;
				}
			}
		}
		return this;
	}

}
