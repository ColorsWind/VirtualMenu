package com.blzeecraft.virtualmenu.core.item;

import java.util.ArrayList;
import java.util.List;

import com.blzeecraft.virtualmenu.core.logger.LogNode;

import lombok.NonNull;

/**
 * 封装的物品构造器
 * @author colors_wind
 *
 * @param <T>
 */
public abstract class AbstractItemBuilder<T> {
	
	protected String id;
	protected int amount;
	protected String name;
	protected List<String> lore;
	protected String nbt;
	
	public AbstractItemBuilder() {}
	
	public AbstractItemBuilder(AbstractItem<T> item) {
		this.id = item.id;
		this.amount = item.amount;
		this.name = item.name;
		this.lore = item.lore;
		this.nbt = item.nbt;
	}
	
	@NonNull
	public AbstractItemBuilder(String id) {
		this.id = id;
	}
	
	public AbstractItemBuilder<T> id(String id) {
		this.id = id;
		return this;
	}
	
	@NonNull
	public AbstractItemBuilder<T> name(String name) {
		this.name = name;
		return this;
	}
	
	@NonNull
	public AbstractItemBuilder<T> lore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	@NonNull
	public AbstractItemBuilder<T> addLore(String... lore) {
		if (this.lore == null) {
			this.lore = new ArrayList<>();
		}
		for(int i=0;i<lore.length;i++) {
			this.lore.add(lore[i]);
		}
		return this;
	}
	
	public AbstractItemBuilder<T> amount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public AbstractItemBuilder<T> removeLore(int index) throws IndexOutOfBoundsException {
		lore.remove(index);
		return this;
	}
	
	@NonNull
	public AbstractItemBuilder<T> nbt(String nbt) {
		this.nbt = nbt;
		return this;
	}
	
	public abstract AbstractItem<T> build(LogNode node);


}
