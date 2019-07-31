package com.blzeecraft.virtualmenu.menu.iiem;


import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bridge.EconomyBridge;
import com.blzeecraft.virtualmenu.bridge.PlayerPointsBridge;
import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ViewItem extends Item {
	
	@Node(key = "VIEW-PERMISSION", type = DataType.STRING)
	protected String viewPermission;

	@Node(key = "VIEW-MONEY", type = DataType.DOUBLE)
	protected double viewMoney;

	@Node(key = "VIEW-POINTS", type = DataType.INT)
	protected int viewPoints;
	
	public boolean canView(Player p) {
		if (viewPermission != null && !p.hasPermission(viewPermission)) {
			return false;
		}
		if (viewMoney > 0D && !EconomyBridge.hasMoney(p, viewMoney)) {
			return false;
		}
		if (viewPoints > 0D && !PlayerPointsBridge.hasPoints(p, viewPoints)) {
			return false;
		}
		return true;
	}

}
