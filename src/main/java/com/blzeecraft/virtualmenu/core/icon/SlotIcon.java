package com.blzeecraft.virtualmenu.core.icon;

import java.util.ArrayList;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

@Data
@AllArgsConstructor
public class SlotIcon {
	
	private final int slot;
	private final Icon icon;
	
	public SlotIcon(int positionX, int positionY, Icon icon) {
		super();
		this.slot = (positionX - 1) + 9 * (positionY - 1); 
		this.icon = icon;
	}
	
	public int getPositionX() {
		return slot % 9 + 1;
	}
	
	public int getPositionY() {
		return slot / 9 + 1;
	}
	
	public SlotIcon combine(SlotIcon another) {
		val icons = new ArrayList<Icon>();
		if (this.icon instanceof MultiIcon) {
			icons.addAll(((MultiIcon) this.icon).getIcons());
		}
		if (another.icon instanceof MultiIcon) {
			icons.addAll(((MultiIcon) another.icon).getIcons());
		}
		Collections.sort(icons);
		Collections.reverse(icons);
		return new SlotIcon(slot, new MultiIcon(icons));
	}
	
	

}
