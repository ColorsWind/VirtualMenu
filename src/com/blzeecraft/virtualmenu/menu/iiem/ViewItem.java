package com.blzeecraft.virtualmenu.menu.iiem;


import org.bukkit.entity.Player;

import com.blzeecraft.virtualmenu.bridge.EconomyBridge;
import com.blzeecraft.virtualmenu.bridge.PlayerPointsBridge;
import com.blzeecraft.virtualmenu.config.DataType;
import com.blzeecraft.virtualmenu.config.Node;
import com.blzeecraft.virtualmenu.logger.PluginLogger;

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

	@Override
	public void apply() throws IllegalArgumentException {
		super.apply();
		if(!EconomyBridge.hasValidEconomy() && viewMoney > 0D) {
			PluginLogger.severe(this, "设置了查看金钱但没有找到经济插件");
		}
		if(!PlayerPointsBridge.hasValidPlugin() && viewPoints > 0) {
			PluginLogger.severe(this, "设置了查看点券但没有找到PlayerPoints插件");
		}
	}
	
	

}
