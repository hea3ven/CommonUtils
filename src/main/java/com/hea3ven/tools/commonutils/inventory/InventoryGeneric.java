package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

import net.minecraftforge.common.util.INBTSerializable;

public class InventoryGeneric implements IInventory, INBTSerializable<NBTTagList> {
	private int size;
	private int stackSize;
	protected final ItemStack[] inv;

	public InventoryGeneric(int size, int stackSize) {
		this.size = size;
		this.stackSize = stackSize;
		inv = new ItemStack[size];
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}

	@Override
	public int getSizeInventory() {
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index >= size)
			return null;
		return inv[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (index >= size)
			return null;
		return inv[index].splitStack(count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index >= size)
			return null;
		ItemStack stack = inv[index];
		inv[index] = null;
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index >= size)
			return;
		inv[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackSize;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index < size;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			inv[i] = null;
		}
	}

	@Override
	public NBTTagList serializeNBT() {
		NBTTagList invNbt = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				NBTTagCompound slotNbt = new NBTTagCompound();
				slotNbt.setByte("Slot", (byte) i);
				inv[i].writeToNBT(slotNbt);
				invNbt.appendTag(slotNbt);
			}
		}
		return invNbt;
	}

	@Override
	public void deserializeNBT(NBTTagList invNbt) {
		for (int i = 0; i < invNbt.tagCount(); ++i) {
			NBTTagCompound slotNbt = invNbt.getCompoundTagAt(i);
			byte slot = slotNbt.getByte("Slot");
			if (0 <= slot && slot < size) {
				inv[slot] = ItemStack.loadItemStackFromNBT(slotNbt);
			}
		}
	}
}
