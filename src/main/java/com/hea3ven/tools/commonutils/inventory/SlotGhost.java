package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGhost extends Slot implements SlotCustom {
	public SlotGhost(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public ItemStack provideItemStack() {
		return null;
	}

	@Override
	public boolean receiveItemStack(ItemStack stack) {
		return false;
	}
}
