package com.blzeecraft.virtualmenu.core.item;

import java.util.Collections;
import java.util.List;

import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.Data;

/**
 * 用于对物品进行包装, 这个类的不可变的.
 * @author colors_wind
 *
 * @param <T>
 */
@Data
public abstract class AbstractItem<T> implements IWrappedObject<T> {
	protected final T handle;
	
	protected final String id;
	protected final int amount;
	protected final String name;
	protected final List<String> lore;
	protected final String nbt;
	
	//protected final String[] copyOfLore;
	
	protected AbstractItem(T handle, String id, int amount, String name, List<String> lore, String nbt) {
		this.handle = handle;
		this.id = id;
		this.amount = amount;
		this.name = name;
		this.lore = lore == null ? Collections.emptyList() : Collections.unmodifiableList(lore);
		this.nbt = nbt;
		//this.copyOfLore = lore.toArray(new String[lore.size()]);
	}
	
	protected AbstractItem(T handle, String id, int amount, String name, List<String> lore) {
		this(handle, id, amount, name, lore, null);
	}
	
	public abstract AbstractItemBuilder<T> builder();
	

	public abstract boolean isEmpty();
	

	
}
