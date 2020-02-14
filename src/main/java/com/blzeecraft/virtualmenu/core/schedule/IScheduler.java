package com.blzeecraft.virtualmenu.core.schedule;

public interface IScheduler {

	void runTaskLater(Runnable task, long delay);

	void runTaskGuaranteePrimaryThread(Runnable task);

	void runTaskAsync(Runnable task);

}
