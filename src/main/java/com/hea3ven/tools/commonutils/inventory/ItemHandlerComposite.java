package com.hea3ven.tools.commonutils.inventory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

public class ItemHandlerComposite implements IItemHandler {
	private List<IItemHandler> handlers = new ArrayList<>();
	private Set<IItemHandler> inputHandlers = new HashSet<>();
	private Set<IItemHandler> outputHandlers = new HashSet<>();

	private int totalSlots = 0;

	public ItemHandlerComposite addInputItemHandler(IItemHandler itemHandler) {
		handlers.add(itemHandler);
		inputHandlers.add(itemHandler);
		totalSlots += itemHandler.getSlots();
		return this;
	}

	public ItemHandlerComposite addOutputItemHandler(IItemHandler itemHandler) {
		handlers.add(itemHandler);
		outputHandlers.add(itemHandler);
		totalSlots += itemHandler.getSlots();
		return this;
	}

	@Override
	public int getSlots() {
		return totalSlots;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		int slots = 0;
		for (IItemHandler handler : handlers) {
			if (slot < slots + handler.getSlots())
				return handler.getStackInSlot(slot - slots);
			slots += handler.getSlots();
		}
		throw new RuntimeException("Slot " + slot + " not in valid range - [0," + totalSlots + ")");
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		int slots = 0;
		for (IItemHandler handler : handlers) {
			if (slot < slots + handler.getSlots())
				return inputHandlers.contains(handler) ?
						handler.insertItem(slot - slots, stack, simulate) : stack;
			slots += handler.getSlots();
		}
		throw new RuntimeException("Slot " + slot + " not in valid range - [0," + totalSlots + ")");
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		int slots = 0;
		for (IItemHandler handler : handlers) {
			if (slot < slots + handler.getSlots())
				return outputHandlers.contains(handler) ?
						handler.extractItem(slot - slots, amount, simulate) : ItemStack.EMPTY;
			slots += handler.getSlots();
		}
		throw new RuntimeException("Slot " + slot + " not in valid range - [0," + totalSlots + ")");
	}

	@Override
	public int getSlotLimit(int slot) {
		int slots = 0;
		for (IItemHandler handler : handlers) {
			if (slot < slots + handler.getSlots())
				return handler.getSlotLimit(slot - slots);
			slots += handler.getSlots();
		}
		throw new RuntimeException("Slot " + slot + " not in valid range - [0," + totalSlots + ")");
	}
}
