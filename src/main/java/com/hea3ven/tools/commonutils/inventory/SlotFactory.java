package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.inventory.Slot;

public interface SlotFactory {
	Slot create(int slot, int x, int y);
}
