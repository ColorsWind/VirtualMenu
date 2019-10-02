package com.blzeecraft.virtualmenu.core.module;

import com.blzeecraft.virtualmenu.core.menu.AbstractPacketMenu;

import lombok.val;

/**
 * 代表刷新菜单变量的任务
 * @author colors_wind
 *
 */
public class MenuRefreshTask implements Runnable {
	protected long tick;

	/**
	 * 应该每tick执行一次
	 */
	@Override
	public void run() {
		tick++;
		for(val en : PacketManager.getOpenMenus().entrySet()) {
			val menu = en.getValue();
			if (menu instanceof AbstractPacketMenu) {
				((AbstractPacketMenu)menu).update(en.getKey(), tick);
			}
		}

	}

}
