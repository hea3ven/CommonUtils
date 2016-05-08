package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

public class SlotLocked extends SlotItemHandlerBase {
	public SlotLocked(IItemHandler inv, int slot, int x, int y) {
		super(inv, slot, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		return null;
	}

	@Override
	public boolean getHasStack() {
		return false;
	}
}
