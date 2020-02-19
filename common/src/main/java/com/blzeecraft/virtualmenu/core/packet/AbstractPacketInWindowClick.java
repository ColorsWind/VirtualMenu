package com.blzeecraft.virtualmenu.core.packet;


import com.blzeecraft.virtualmenu.core.item.AbstractItem;
import com.blzeecraft.virtualmenu.core.menu.ClickType;

import lombok.ToString;

/**
 * 代表玩家点击菜单时客户端发送给服务端的 Packet.
 * @author colors_wind
 *
 */
@ToString(callSuper = true)
public abstract class AbstractPacketInWindowClick<T> extends AbstractWindowPacket<T> {
	

	public AbstractPacketInWindowClick(T handle) {
		super(handle);
	}
	
	public abstract void setRawSlot(int slot);
	public abstract int getRawSlot();
	
	public abstract void setClickMode(ClickMode mode);
	public abstract ClickMode getClickMode();
	
	public abstract void setButton(int button);
	public abstract int getButton();
	
	public abstract void setClickedItem(AbstractItem<?> item);
	public abstract AbstractItem<?> getClickedItem();
	
	public ClickType getClickType(){
		int button = getButton();
		//maybe you need to check slot before invoke this method
		int slot = getRawSlot(); 
		switch(getClickMode()) {
		case PICKUP: 
			if (button == 0) {
				return ClickType.LEFT;
			} else if (button == 1) {
				return ClickType.RIGHT;
			}
			break;
		case QUICK_MOVE:
			if (button == 0) {
				return ClickType.SHIFT_LEFT;
			} else if (button == 1) {
				return ClickType.SHIFT_RIGHT;
			}
			break;
		case SWAP:
			return ClickType.NUMBER_KEY;
		case CLONE:
			return ClickType.MIDDLE;
		case THROW:
			if (slot >= 0) {
				if (button == 0) {
					return ClickType.DROP;
				} else if (button == 1) {
					return ClickType.CONTROL_DROP;
				}
			} else if (slot == -999) {
				if (button == 0) {
					return ClickType.WINDOW_BORDER_LEFT;
				} else if (button == 1) {
					return ClickType.WINDOW_BORDER_RIGHT;
				}
			}
			break;
		case QUICK_CRAFT:
			if (slot >= 0) {
				if (button == 1) {
					return ClickType.LEFT;
				} else if (button == 5) {
					return ClickType.RIGHT;
				}
			} else if (slot == -999) {
				switch(button) {
				case 0:
				case 1:
				case 2:
					return ClickType.LEFT;
				case 4:
				case 5:
				case 6:
					return ClickType.RIGHT;
				case 8:
				case 9:
				case 10:
					return ClickType.MIDDLE;
				}
			}
		case PICKUP_ALL:
			return ClickType.DOUBLE_CLICK;
		}
		return ClickType.UNKNOWN;
	}

}
