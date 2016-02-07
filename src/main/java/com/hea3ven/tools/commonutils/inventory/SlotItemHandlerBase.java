package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerBase extends SlotItemHandler {
	public SlotItemHandlerBase(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public void putStack(ItemStack stack) {
		itemHandler.insertItem(getSlotIndex(), stack, false);
		onSlotChanged();
	}
}
