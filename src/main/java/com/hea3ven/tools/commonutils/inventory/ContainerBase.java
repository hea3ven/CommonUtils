package com.hea3ven.tools.commonutils.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public abstract class ContainerBase extends Container {

	public void addSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize, int ySize) {
		addSlots(xOff, yOff, xSize, ySize,
				(slot, x, y) -> new SlotItemHandler(inv, slotOff + slot, x, y));
	}

	public void addSlots(int xOff, int yOff, int xSize, int ySize, SlotSupplier slotSupplier) {
		for (int y = 0; y < ySize; ++y) {
			for (int x = 0; x < xSize; ++x) {
				this.addSlotToContainer(slotSupplier.get(x + y * xSize, xOff + x * 18, yOff + y * 18));
			}
		}
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = player.inventory;

		if (clickTypeIn == ClickType.QUICK_CRAFT) {
			return super.slotClick(slotId, clickedButton, clickTypeIn, player);
		} else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) &&
				(clickedButton == 0 || clickedButton == 1)) {
			if (slotId == -999) {
				if (inventoryplayer.getItemStack() != null) {
					if (clickedButton == 0) {
						player.dropItem(inventoryplayer.getItemStack(), true);
						inventoryplayer.setItemStack(null);
					}

					if (clickedButton == 1) {
						player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);

						if (inventoryplayer.getItemStack().stackSize == 0) {
							inventoryplayer.setItemStack(null);
						}
					}
				}
			} else if (clickTypeIn == ClickType.QUICK_MOVE) {
				IAdvancedSlot slot = getAdvancedSlot(slotId);
				if (slot != null)
					itemstack = slot.onQuickMove(this, player, clickedButton);
			} else {
				IAdvancedSlot slot = getAdvancedSlot(slotId);
				if (slot != null)
					itemstack = slot.onPickUp(player, clickedButton);
			}
		} else if (clickTypeIn == ClickType.SWAP && clickedButton >= 0 && clickedButton < 9) {
			IAdvancedSlot slot = getAdvancedSlot(slotId);
			if (slot != null)
				slot.onSwapPlayerStack(player, clickedButton);
		} else if (clickTypeIn == ClickType.CLONE) {
			IAdvancedSlot slot = getAdvancedSlot(slotId);
			if (slot != null)
				slot.onClone(player);
		} else if (clickTypeIn == ClickType.THROW) {
			IAdvancedSlot slot = getAdvancedSlot(slotId);
			if (slot != null)
				slot.onThrow(player, clickedButton);
		} else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0) {
			IAdvancedSlot slot = getAdvancedSlot(slotId);
			if (slot != null)
				slot.onPickUpAll(this, player, clickedButton);
		}

		return itemstack;
	}

	@Nullable
	public IAdvancedSlot getAdvancedSlot(int slotId) {
		if (slotId < 0)
			return null;

		Slot slot = getSlot(slotId);
		return getAdvancedSlot(slot);
	}

	@Nonnull
	private IAdvancedSlot getAdvancedSlot(Slot slot) {
		return (slot instanceof IAdvancedSlot) ? (IAdvancedSlot) slot : new AdvancedSlotWrapper(slot);
	}

	public void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
		super.retrySlotClick(slotId, clickedButton, mode, playerIn);
	}

	@Override
	public boolean canDragIntoSlot(Slot slot) {
		return getAdvancedSlot(slot).canDragIntoSlot();
	}

	protected boolean mergeSlot(IAdvancedSlot slot, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;
		int step = 1;
		int end = endIndex;

		for (int attempt = 0; attempt < 2; attempt++) {
			if(attempt == 0 && !slot.getImmutableStack().isStackable())
				continue;
			if (reverseDirection) {
				i = endIndex - 1;
				step = -1;
				end = startIndex - 1;
			}

			while (slot.canTransferFromSlot() && i != end) {
				IAdvancedSlot targetSlot = getAdvancedSlot(i);
				if (targetSlot != null) {
					if ((attempt == 0 && targetSlot.getImmutableStack() != null) ||
							(attempt == 1 && targetSlot.getImmutableStack() == null)) {
						if (targetSlot.transferFrom(slot)) {
							flag = true;
						}
					}
				}

				i += step;
			}
		}
		return flag;
	}
}
