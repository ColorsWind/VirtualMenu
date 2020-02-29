package com.blzeecraft.virtualmenu.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import com.blzeecraft.virtualmenu.core.schedule.AbstractTask;
import com.blzeecraft.virtualmenu.core.schedule.IScheduler;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class WrapSchedulerBukkit implements IScheduler {
	protected final VirtualMenuPlugin plugin;
	protected final BukkitScheduler scheduler = Bukkit.getScheduler();

	@Override
	public AbstractTask<?> runTaskLater(Runnable task, long delay) {
		return new WrapTaskBukkit(scheduler.runTaskLater(plugin, task, delay));
	}
	
	@Override
	public AbstractTask<?> runTaskTimer(Runnable task, long delay, long period) {
		return new WrapTaskBukkit(scheduler.runTaskTimer(plugin, task, delay, period));
	}

	@Override
	public AbstractTask<?> runTaskGuaranteePrimaryThread(Runnable task) {
		if (Bukkit.isPrimaryThread()) {
			task.run();
			return new SyncTask(task);
		} else {
			return new WrapTaskBukkit(scheduler.runTask(plugin, task));
		}
	}

	@Override
	public AbstractTask<?> runTaskAsync(Runnable task) {
		return new WrapTaskBukkit(scheduler.runTaskAsynchronously(plugin, task));
	}

	class WrapTaskBukkit extends AbstractTask<BukkitTask> {

		public WrapTaskBukkit(BukkitTask handle) {
			super(handle);
		}

		@Override
		public boolean isRunning() {
			return handle.isCancelled();
		}

		@Override
		public boolean isAsync() {
			return !handle.isSync();
		}

		@Override
		public void cancel() {
			handle.cancel();
		}
	}

	class SyncTask extends AbstractTask<Runnable> {

		public SyncTask(Runnable handle) {
			super(handle);
		}

		@Override
		public boolean isRunning() {
			return false;
		}

		@Override
		public boolean isAsync() {
			return false;
		}

		@Override
		public void cancel() {
		}

	}



}
