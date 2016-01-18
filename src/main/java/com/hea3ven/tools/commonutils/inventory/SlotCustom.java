package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.item.ItemStack;

public interface SlotCustom {

	ItemStack provideItemStack();

	boolean receiveItemStack(ItemStack stack);
}
