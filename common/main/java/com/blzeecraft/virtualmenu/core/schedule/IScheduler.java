package com.blzeecraft.virtualmenu.core.schedule;

public interface IScheduler {

	AbstractTask<?> runTaskLater(Runnable task, long delay);

	AbstractTask<?> runTaskGuaranteePrimaryThread(Runnable task);

	AbstractTask<?> runTaskAsync(Runnable task);

}
