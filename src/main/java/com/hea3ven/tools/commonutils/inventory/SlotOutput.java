package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

public class SlotOutput extends SlotItemHandlerBase {

	public SlotOutput(IItemHandler inv, int index, int xPosition, int yPosition) {
		super(inv, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
