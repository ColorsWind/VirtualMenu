package com.blzeecraft.virtualmenu.core.menu;

/**
 * 代表刷新菜单变量的任务
 * @author colors_wind
 *
 */
public class UpdateIconTask implements Runnable {
	protected long tick;

	/**
	 * 应该每tick执行一次
	 */
	@Override
	public void run() {
		tick++;

	}

}
