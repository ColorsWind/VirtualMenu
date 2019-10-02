package com.blzeecraft.virtualmenu.core.item;

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
