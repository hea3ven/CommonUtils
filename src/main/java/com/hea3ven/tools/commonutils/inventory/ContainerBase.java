package com.hea3ven.tools.commonutils.inventory;

import java.lang.reflect.Constructor;

import com.google.common.base.Throwables;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerBase extends Container {

	public void addInventoryGrid(IInventory inv, int slotOff, int xOff, int yOff, int xSize, int ySize) {
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, Slot.class, inv);
	}

	public void addInventoryGrid(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize, int ySize) {
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotItemHandler.class, inv);
	}

	public void addInventoryGrid(int slotOff, int xOff, int yOff, int xSize, int ySize,
			Class<? extends Slot> cls, Object... args) {
		try {
			Class<?>[] argsTypes = new Class[args.length + 3];
			int i = 0;
			for (; i < args.length; i++)
				argsTypes[i] = args[i].getClass();
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i++] = Integer.TYPE;
			argsTypes[i] = Integer.TYPE;

			Constructor<? extends Slot> ctor =
					ConstructorUtils.getMatchingAccessibleConstructor(cls, argsTypes);
			for (int y = 0; y < ySize; ++y) {
				for (int x = 0; x < xSize; ++x) {
					Object[] objArgs = new Object[args.length + 3];
					for (i = 0; i < args.length; i++)
						objArgs[i] = args[i];
					objArgs[i++] = slotOff + x + y * xSize;
					objArgs[i++] = xOff + x * 18;
					objArgs[i] = yOff + y * 18;
					this.addSlotToContainer(ctor.newInstance(objArgs));
				}
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	public void addInventoryGrid(int slotOff, int xOff, int yOff, int xSize, int ySize,
			SlotFactory slotFactory) {
		for (int y = 0; y < ySize; ++y) {
			for (int x = 0; x < xSize; ++x) {
				this.addSlotToContainer(
						slotFactory.create(slotOff + x + y * xSize, xOff + x * 18, yOff + y * 18));
			}
		}
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex,
			boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (stack.stackSize > 0 &&
					(!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if (itemstack != null && itemstack.getItem() == stack.getItem() &&
						(!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) &&
						ItemStack.areItemStackTagsEqual(stack, itemstack)) {
					if (slot instanceof SlotItemHandler) {
						ItemStack restStack =
								((SlotItemHandler) slot).itemHandler.insertItem(slot.getSlotIndex(),
										stack.copy(), false);
						if (restStack == null)
							stack.stackSize = 0;
						else
							stack.stackSize = restStack.stackSize;
						flag = true;
						break;
					} else {
						int j = itemstack.stackSize + stack.stackSize;

						if (j <= stack.getMaxStackSize()) {
							stack.stackSize = 0;
							itemstack.stackSize = j;
							slot.onSlotChanged();
							flag = true;
						} else if (itemstack.stackSize < stack.getMaxStackSize()) {
							stack.stackSize -= stack.getMaxStackSize() - itemstack.stackSize;
							itemstack.stackSize = stack.getMaxStackSize();
							slot.onSlotChanged();
							flag = true;
						}
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (stack.stackSize > 0) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1 == null &&
						slot1.isItemValid(stack)) // Forge: Make sure to respect isItemValid in the slot.
				{
					if (slot1 instanceof SlotItemHandler) {
						ItemStack restStack =
								((SlotItemHandler) slot1).itemHandler.insertItem(slot1.getSlotIndex(),
										stack.copy(), false);
						if (restStack == null)
							stack.stackSize = 0;
						else
							stack.stackSize = restStack.stackSize;
						flag = true;
						break;
					} else {
						slot1.putStack(stack.copy());
						slot1.onSlotChanged();
						stack.stackSize = 0;
						flag = true;
						break;
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}
}
