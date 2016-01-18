package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInputOutput extends Slot {

	public SlotInputOutput(IInventory inv, int slot, int xPos, int yPos) {
		super(inv, slot, xPos, yPos);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return inventory.isItemValidForSlot(slotNumber, stack);
	}
}
