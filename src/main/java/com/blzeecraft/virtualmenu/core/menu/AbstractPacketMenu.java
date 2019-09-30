package com.blzeecraft.virtualmenu.core.menu;

import java.util.Collection;

import com.blzeecraft.virtualmenu.core.IUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public abstract class AbstractPacketMenu implements IPacketMenu {
	
	protected final String title;
	protected final IMenuType type;
	protected final Icon[] icons;
	
	public AbstractPacketMenu(String title, IMenuType type) {
		this.title = title;
		this.type = type;
		this.icons = new Icon[type.size()];
	}



	@Override
	public void click(ClickEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(IUser<?> user, int slot) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addViewer(IUser<?> user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeViewer(IUser<?> user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<IUser<?>> getViewers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractItem<?> viewItem(IUser<?> user, int slot) {
		// TODO Auto-generated method stub
		return null;
	}


}
