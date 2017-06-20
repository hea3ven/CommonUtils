package com.hea3ven.tools.commonutils.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import net.minecraftforge.common.util.Constants.NBT;

public class InventoryUtil {
	public static NBTTagCompound serializeNBT(IInventory inv) {
		NBTTagList slotsNbt = new NBTTagList();
		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);
			if (!stack.isEmpty()) {
				NBTTagCompound slotNbt = new NBTTagCompound();
				slotNbt.setByte("Slot", (byte) slot);
				stack.writeToNBT(slotNbt);
				slotsNbt.appendTag(slotNbt);
			}
		}
		NBTTagCompound output = new NBTTagCompound();
		output.setTag("Inventory", slotsNbt);
		return output;
	}

	public static void deserializeNBT(IInventory inv, NBTTagCompound compound) {
		NBTTagList slotsNbt = compound.getTagList("Inventory", NBT.TAG_COMPOUND);
		for (int i = 0; i < slotsNbt.tagCount(); i++) {
			NBTTagCompound slotNbt = slotsNbt.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(slotNbt);
			inv.setInventorySlotContents(slotNbt.getByte("Slot"), stack);
		}
	}
}
