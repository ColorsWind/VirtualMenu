package com.blzeecraft.virtualmenu.core.user;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

import com.blzeecraft.virtualmenu.core.packet.PacketManager;
import com.blzeecraft.virtualmenu.core.IWrappedObject;
import com.blzeecraft.virtualmenu.core.VirtualMenu;
import com.blzeecraft.virtualmenu.core.menu.IPacketMenu;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * 对玩家,控制台进行包装
 * 
 * @author colors_wind
 *
 * @param <T> 玩家对应的类型
 */
public interface IUser<T> extends IWrappedObject<T> {

	/**
	 * 获取用户名.
	 * 
	 * @return 用户名
	 */
	String getName();

	/**
	 * 获取用户的UUID.
	 * 
	 * @return 用户的UUID.
	 */
	UUID getUniqueId();

	/**
	 * 给用户发送消息.
	 * 
	 * @param component
	 */
	void sendMessage(BaseComponent... components);
	
	/**
	 * 给用户发送消息.
	 * 
	 * @param msg
	 */
	default void sendMessageWithPrefix(String msg) {
		this.sendMessage(new StringBuilder(VirtualMenu.PREFIX.length() + msg.length()).append(VirtualMenu.PREFIX)
				.append(msg).toString());
	}

	/**
	 * 给用户发送消息.
	 * 
	 * @param msg
	 */
	void sendMessage(String msg);

	/**
	 * 给用户发送消息.
	 * 
	 * @param msg
	 */
	default void sendMessage(String... msg) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<msg.length;i++) {
			sb.append(msg[i]);
		}
		this.sendMessage(sb.toString());
	}
	
	/**
	 * 给用户玩家带插件前缀的消息.
	 * 
	 * @param msg
	 */
	default void sendMessageWithPrefix(String... msg) {
		StringBuilder sb = new StringBuilder(VirtualMenu.PREFIX);
		for(int i=0;i<msg.length;i++) {
			sb.append(msg[i]);
		}
		this.sendMessage(sb.toString());
	}

	/**
	 * 在指定的通道向接收者发送插件消息.
	 * 
	 * @param channel
	 * @param byteArray
	 */
	void sendPluginMessage(String channel, byte[] byteArray);

	/**
	 * 判断用户是否拥有权限.
	 * 
	 * @param permission
	 * @return {@code true} 如果返回拥有这个权限, 否则返回 {@code false}.
	 */
	boolean hasPermission(String permission);

	/**
	 * 获取玩家的等级.
	 * 
	 * @return 玩家等级
	 */
	int getLevel();

	/**
	 * 设置玩家等级.
	 * 
	 * @param level
	 */
	void setLevel(int level);

	/**
	 * 获取玩家指定货币的余额.
	 * 
	 * @param currency 货币名称,要使用默认货币,请传入 {@code null}
	 * @return 货币余额, 如果该种货币不存在, 返回 {@link OptionalDouble#empty()}.
	 */
	OptionalDouble getBanlance(String currency);

	/**
	 * 向玩家指定货币账户存款.
	 * 
	 * @param currency 货币名称
	 * @param amount   存款金额
	 * @return {@code true} 如果存款成功, 否则返回 {@code false}.
	 */
	boolean deposit(String currency, double amount);

	/**
	 * 向玩家指定货币账户取款.
	 * 
	 * @param currency 货币名称
	 * @param amount   取款金额
	 * @return {@code true} 如果取款成功, 否则返回 {@code false}.
	 */
	boolean withdraw(String currency, double amount);

	/**
	 * 使用户执行指定指令.
	 * 
	 * @param command 命令(不带 "/")
	 * @return {@code true} 如果执行成功, 否则返回 {@code false}.
	 */
	boolean performCommand(String command);

	/**
	 * 以管理员身份执行指定命令.
	 * 
	 * @param command 命令(不带 "/")
	 * @return {@code true} 如果执行成功, 否则返回 {@code false}.
	 */
	boolean performCommandAsAdmin(String command);

	/**
	 * 向玩家发送 ActionBar.
	 * 
	 * @param actionbar
	 */
	void sendActionbar(String actionbar);

	/**
	 * 向玩家发送 Title.
	 * 
	 * @param title 主标题
	 */
	void sendTitle(String title);

	/**
	 * 向玩家发送 Title.
	 * 
	 * @param title    主标题
	 * @param subTitle 副标题
	 * @param fadeIn   淡入时间(单位tick)
	 * @param stay     保持时间(单位tick)
	 * @param fadeOut  淡出时间(单位tick)
	 */
	void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut);

	/**
	 * 向玩家所在位置播放一个声音.
	 * 
	 * @param sound  声音
	 * @param volume 音量 默认 1F
	 * @param pitch  音高 默认 0F
	 */
	void playSound(String sound, float volume, float pitch);

	/**
	 * 使玩家打开 PacketMenu.
	 * 
	 * @param menu
	 * @see {@link PacketManager#openMenu(IUser, IPacketMenu)}
	 */
	default void openPacketMenu(IPacketMenu menu) {
		PacketManager.openMenu(this, menu);
	}

	/**
	 * 获取玩家当前打开的 PacketMenu.
	 * 
	 * @return 目前打开的 PacketMenu, 如果玩家没有打开 PacketMenu, 返回 {@link Optional#empty()};
	 */
	default Optional<IPacketMenu> getOpenPacketMenu() {
		return this.getCurrentSession().map(UserSession::getMenu);
	}

	/**
	 * 关闭玩家当前打开的 PacketMenu.
	 */
	default void closePacketMenu() {
		PacketManager.closePacketMenu(this);
	}

	/**
	 * 关闭玩家打开的 Inventory
	 */
	default void closeInventory() {
		PacketManager.closeInventory(this);
	}

	/**
	 * 获取用户当前的 UserSession, 这个方法支持并发调用.
	 * 
	 * @return 当前的 UserSession.
	 */
	Optional<UserSession> getCurrentSession();

	/**
	 * 设置用户的 UserSession, 这个方法支持并发调用.
	 * 
	 * @param session
	 */
	void setCurrentSession(UserSession session);

	/**
	 * 判断用户是否未控制台.
	 * 
	 * @return {@code true} 如果用户是控制台, 否则返回 {@code false}.
	 */
	boolean isConsole();

}
