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
		ItemStack slotStack = getItemHandler().getStackInSlot(getSlotIndex());
		if (slotStack != null) {
			if(slotStack.stackSize == 0)
				slotStack.stackSize = 1;
			getItemHandler().extractItem(getSlotIndex(), slotStack.stackSize, false);
		}
		getItemHandler().insertItem(getSlotIndex(), stack, false);
		onSlotChanged();
	}
}
