package com.blzeecraft.virtualmenu.core.variable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.animation.EnumUpdateDelay;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;
import com.blzeecraft.virtualmenu.core.packet.IPacketAdapter;
import com.blzeecraft.virtualmenu.core.schedule.AbstractTask;
import com.blzeecraft.virtualmenu.core.user.UserSession;

import lombok.val;

public class VariableUpdater implements Runnable {
	public static LogNode LOG_NODE = LogNode.of("#VariableUpdater");
	private final EnumUpdateDelay delay;
	private final Set<UserSession> sessions;
	private final IPacketAdapter packetAdapter;
	private AbstractTask<?> task;

	public VariableUpdater(EnumUpdateDelay delay) {
		this.delay = delay;
		sessions = new HashSet<>();
		this.packetAdapter = VirtualMenu.getPacketAdapter();
	}

	@Override
	public void run() {
		for (val session : sessions) {
			IPacketMenu menu = session.getMenu();
			Object[] rawItems = menu.viewRawItems(session);
			session.updateCachePacketMenuRawItems(rawItems);
			val packet = session.createPacketWindowItemsForMenu();
			try {
				packetAdapter.sendServerPacket(session.getUser(), packet);
			} catch (InvocationTargetException e) {
				PluginLogger.warning(LOG_NODE, "发送更新菜单 Item 的 Packet 错误.");
				e.printStackTrace();
			}
		}
	}

	public VariableUpdater start() {
		task = VirtualMenu.getScheduler().runTaskTimer(this, delay.getDelay() / 2, delay.getDelay());
		return this;
	}
	
	public void stop() {
		if (task != null && task.isRunning()) {
			task.cancel();
		}
	}
	
	
	public static Map<EnumUpdateDelay, VariableUpdater> UPDATER_MAP = new EnumMap<>(EnumUpdateDelay.class);
	
	public static void startUpdate() {
		if (UPDATER_MAP.size() != 0) {
			PluginLogger.warning(LOG_NODE, "刷新菜单任务已经开始. 无法重复开始.");
			throw new IllegalArgumentException();
		}
		Arrays.stream(EnumUpdateDelay.values()).forEach(delay -> {
			val updater = new VariableUpdater(delay).start();
			UPDATER_MAP.put(delay, updater);
		});
	}
	
	public static void stopUpdate() {
		if (UPDATER_MAP.size() == 0) {
			PluginLogger.warning(LOG_NODE, "刷新菜单任务未开始. 不能停止.");
			throw new IllegalArgumentException();
		}
		UPDATER_MAP.values().forEach(VariableUpdater::stop);
		UPDATER_MAP.clear();
	}
	
	public static void addUpdateTask(UserSession session) {
		val menu = session.getMenu();
		UPDATER_MAP.get(menu.getUpdateDelay()).sessions.add(session);
	}
	
	public static void removeUpdateTask(UserSession session) {
		val menu = session.getMenu();
		UPDATER_MAP.get(menu.getUpdateDelay()).sessions.remove(session);
	}
	

}
