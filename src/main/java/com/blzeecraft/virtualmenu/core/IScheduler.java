package com.blzeecraft.virtualmenu.core;

public interface IScheduler {

	void runTaskLater(Runnable task, long delay);

	void runTaskGuaranteePrimaryThread(Runnable task);

}
