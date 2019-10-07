package com.blzeecraft.virtualmenu.core.config.map;

import static com.blzeecraft.virtualmenu.core.config.map.DataType.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.blzeecraft.virtualmenu.core.action.Actions;
import com.blzeecraft.virtualmenu.core.adapter.VirtualMenu;
import com.blzeecraft.virtualmenu.core.condition.Conditions;
import com.blzeecraft.virtualmenu.core.config.MissingRequiredObjectException;
import com.blzeecraft.virtualmenu.core.icon.DynamicIcon;
import com.blzeecraft.virtualmenu.core.icon.Icon;
import com.blzeecraft.virtualmenu.core.icon.SimpleIcon;
import com.blzeecraft.virtualmenu.core.icon.SlotIcon;
import com.blzeecraft.virtualmenu.core.logger.LogNode;
import com.blzeecraft.virtualmenu.core.module.VariableManager;

import lombok.val;

public class IconTemplate implements ITemplate<SlotIcon> {
	
	@Node(key = "id", type = STRING)
	public String id;
	
	@Node(key = "priority", type = INTEGER)
	public OptionalInt priority;
	
	@Node(key = "name", type = STRING)
	public Optional<String> name;
	
	@Node(key = "lore", type = STRING_LIST)
	public Optional<List<String>> lore;
	
	@Node(key = "amount", type = INTEGER)
	public OptionalInt amount;
	
	@Node(key = "nbt", type = STRING)
	public Optional<String> nbt;
	
	@Node(key = "variable", type = STRING_LIST)
	public Optional<List<String>> variable;
	
	@Node(key = "position-x", type = INTEGER)
	public OptionalInt positionX;
	
	@Node(key = "position-y", type = INTEGER)
	public OptionalInt positionY;
	
	@Node(key = "slot", type = INTEGER)
	public OptionalInt positionSlot;
	
	@Node(key = "action", type = STRING_LIST)
	public Optional<List<String>> action;
	
	@Node(key = "condition", type = STRING_LIST)
	public Optional<List<String>> condition;
	
	@Node(key = "view-condition", type = STRING_LIST)
	public Optional<List<String>> viewCondition;

	@Override
	public SlotIcon apply(LogNode node) {
		// 基本信息: 位置 & 物品
		val priority = priority.orElse(0);
		final int slot;
		if (positionSlot.isPresent()) {
			slot = positionSlot.getAsInt();
		} else if (positionX.isPresent() && positionY.isPresent()) {
			slot = (positionX.getAsInt() - 1) + 9 * (positionY.getAsInt() -1);
		} else {
			throw new MissingRequiredObjectException("未设置图标位置, slot和position-xy 最少设置一项");
		}
		val builder = VirtualMenu.createItemBuilder().id(id);
		this.name.ifPresent(name -> builder.name(name));
		this.lore.ifPresent(lore -> builder.lore(lore));
		this.amount.ifPresent(amount -> builder.amount(amount));
		this.nbt.ifPresent(nbt -> builder.nbt(nbt));
		val item = builder.build();
		
		
		// 点击相关: 查看条件 & 点击条件 & 点击动作
		val viewCondition = Conditions.parse(node, viewCondition.orElse(Collections.emptyList()));
		val clickCondition = Conditions.parse(node, condition.orElse(Collections.emptyList()));
		val action = Actions.parse(node, action.orElse(Collections.emptyList()));

		// 下面开始构造图标
		final Icon icon = variable.map(vars -> {
			val name = this.name.map(s -> VariableManager.getVariable(node, s, vars)).orElse(DynamicIcon.EMPTY_NAME);
			val lore = this.lore.map(l -> VariableManager.getVariable(node, l, vars)).orElse(DynamicIcon.EMPTY_LORE);
			return (Icon)new DynamicIcon(priority, item, clickCondition, viewCondition, action, name, lore);
		}).orElse(new SimpleIcon(item, clickCondition, clickCondition, action));
		
		return new SlotIcon(slot, icon);
	}

}
