package com.blzeecraft.virtualmenu.core.menu;

/**
 * 玩家点击菜单的方式
 * @author colors_wind
 *
 */
public enum ClickType {
	LEFT, SHIFT_LEFT, RIGHT, SHIFT_RIGHT, WINDOW_BORDER_LEFT, WINDOW_BORDER_RIGHT, MIDDLE, NUMBER_KEY, DOUBLE_CLICK, DROP, CONTROL_DROP, CREATIVE, UNKNOWN;

	public boolean isKeyboardClick() {
		return this == NUMBER_KEY || this == DROP || this == CONTROL_DROP;
	}

	public boolean isCreativeAction() {
		return this == MIDDLE || this == CREATIVE;
	}

	public boolean isRightClick() {
		return this == RIGHT || this == SHIFT_RIGHT;
	}

	public boolean isLeftClick() {
		return this == LEFT || this == SHIFT_LEFT || this == DOUBLE_CLICK || this == CREATIVE;
	}

	public boolean isShiftClick() {
		return this == SHIFT_LEFT || this == SHIFT_RIGHT || this == CONTROL_DROP;
	}
	
	public boolean isBorderClick() {
		return this == WINDOW_BORDER_LEFT || this == WINDOW_BORDER_RIGHT;
	}
}