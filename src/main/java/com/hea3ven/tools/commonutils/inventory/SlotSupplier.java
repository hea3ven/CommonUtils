package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.inventory.Slot;

@FunctionalInterface
public interface SlotSupplier {
	Slot get(int slot, int x, int y);
}
