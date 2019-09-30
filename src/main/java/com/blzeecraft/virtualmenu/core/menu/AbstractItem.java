package com.blzeecraft.virtualmenu.core.menu;

import java.util.List;

import com.blzeecraft.virtualmenu.core.IWrappedObject;

import lombok.Data;

@Data
public abstract class AbstractItem<T> implements IWrappedObject<T> {
	protected String type;
	protected int amount;
	protected String displayname;
	protected List<String> lore;
	

	
}
