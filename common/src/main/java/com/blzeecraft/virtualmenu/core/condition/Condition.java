package com.blzeecraft.virtualmenu.core.condition;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import com.blzeecraft.virtualmenu.core.action.event.MenuEvent;
import com.blzeecraft.virtualmenu.core.conf.line.LineConfigObject;
import com.blzeecraft.virtualmenu.core.conf.line.ResolvedLineConfig;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.logger.LoggerObject;
import com.blzeecraft.virtualmenu.core.logger.PluginLogger;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

/**
 * 这个类代表一个条件.这个类是不可变的.<p>
 * 条件是诸如玩家等级,拥有的权限,持有的金钱等.<p>
 * 条件分为两种:view-condition和click-condition,都在icon下配置.<p>
 * 检查条件时不推荐对玩家进行有副作用的操作(如检查玩家金钱时扣除金钱),
 * 若条件在click-condition中设置:条件可能不只一个,任何一个条件不满足都不会继续执行,
 * 若条件在view-condition中设置:插件不能保证view-condition中条件被检查的次数.
 * 
 * @see ICondition
 * @author colors_wind
 * @date 2020-02-10
 */
public abstract class Condition extends LineConfigObject implements ICondition, LoggerObject {

	protected final String message;
	protected final Set<ClickType> types;

	public Condition(LogNode node, ResolvedLineConfig rlc) {
		super(node);
		this.message = rlc.getAsOptString("msg").orElse("");
		this.types = rlc.getAsOptEnumSet("click", ClickType.class).orElse(EnumSet.allOf(ClickType.class));
	}

	public Condition(LogNode node) {
		super(node);
		this.message = "";
		this.types = EnumSet.allOf(ClickType.class);
	}

	@Override
	public LogNode getLogNode() {
		return node;
	}
	
	

	@Override
	public Optional<String> apply(MenuEvent event) {
		if (types.contains(event.getClickType())) {
			return Optional.of(message);
		}
		try {
			return check(event);
		} catch (Exception ex) {
			ex.printStackTrace();
			PluginLogger.warning(node, "检查条件时发生严重错误, 该条件检查不通过.");
		}
		return Optional.of("发送严重错误, 请联系管理员.");
	}

	public abstract Optional<String> check(MenuEvent event);

	@Override
	public boolean test(MenuEvent event) {
		return !this.apply(event).isPresent();
	}

	
	
}
