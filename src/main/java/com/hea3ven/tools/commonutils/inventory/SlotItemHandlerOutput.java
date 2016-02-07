package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerOutput extends SlotItemHandlerBase {

	public SlotItemHandlerOutput(IItemHandler inv, int index, int xPosition, int yPosition) {
		super(inv, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
