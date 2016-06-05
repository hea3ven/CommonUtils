package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.hea3ven.tools.commonutils.util.ItemStackUtil;

public class AdvancedSlotWrapper implements IAdvancedSlot {
	private final Slot slot;

	public AdvancedSlotWrapper(Slot slot) {
		this.slot = slot;
	}

	@Override
	public ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton) {

		ItemStack resultStack = null;
		if (slot.canTakeStack(player)) {
			ItemStack slotStack = slot.getStack();

			if (slotStack != null && slotStack.stackSize <= 0) {
				resultStack = slotStack.copy();
				slot.putStack(null);
			}

			ItemStack extraStack = container.transferStackInSlot(player, slot.slotNumber);

			if (extraStack != null) {
				Item item = extraStack.getItem();
				resultStack = extraStack.copy();

				if (slot.getStack() != null && slot.getStack().getItem() == item) {
					container.retrySlotClick(slot.slotNumber, clickedButton, true, player);
				}
			}
		}
		return resultStack;
	}

	@Override
	public ItemStack onPickUp(EntityPlayer player, int clickedButton) {
		ItemStack slotStack = slot.getStack();
		ItemStack playerStack = player.inventory.getItemStack();

		ItemStack resultStack = null;
		if (slotStack != null) {
			resultStack = slotStack.copy();
		}

		if (slotStack == null) {
			putPlayerStackInSlot(clickedButton, player.inventory, playerStack);
		} else if (slot.canTakeStack(player)) {
			if (playerStack == null) {
				putSlotStackInPlayer(player, clickedButton, player.inventory, slotStack);
			} else if (slot.isItemValid(playerStack)) {
				if (ItemStackUtil.areItemsCompletelyEqual(slotStack, playerStack)) {
					mergePlayerStackIntoSlot(clickedButton, player.inventory, slotStack, playerStack);
				} else if (playerStack.stackSize <= slot.getItemStackLimit(playerStack)) {
					swapPlayerAndSlotStacks(player.inventory, slotStack, playerStack);
				}
			} else if (slotStack.getItem() == playerStack.getItem() &&
					playerStack.getMaxStackSize() > 1 && (!slotStack.getHasSubtypes() ||
					slotStack.getMetadata() == playerStack.getMetadata()) &&
					ItemStack.areItemStackTagsEqual(slotStack, playerStack)) {
				mergeSlotStackIntoPlayer(player, player.inventory, slotStack, playerStack);
			}
		}

		slot.onSlotChanged();
		return resultStack;
	}

	private void putPlayerStackInSlot(int clickedButton, InventoryPlayer playerInv, ItemStack playerStack) {
		if (playerStack != null && slot.isItemValid(playerStack)) {
			int putSize = clickedButton == 0 ? playerStack.stackSize : 1;

			if (putSize > slot.getItemStackLimit(playerStack)) {
				putSize = slot.getItemStackLimit(playerStack);
			}

			slot.putStack(playerStack.splitStack(putSize));

			if (playerStack.stackSize == 0) {
				playerInv.setItemStack(null);
			}
		}
	}

	private void putSlotStackInPlayer(EntityPlayer player, int clickedButton, InventoryPlayer playerInv,
			ItemStack slotStack) {
		if (slotStack.stackSize > 0) {
			int k2 =
					clickedButton == 0 ? slotStack.stackSize : (slotStack.stackSize + 1) / 2;
			playerInv.setItemStack(slot.decrStackSize(k2));

			if (slotStack.stackSize <= 0) {
				slot.putStack(null);
			}

			slot.onPickupFromSlot(player, playerInv.getItemStack());
		} else {
			slot.putStack(null);
			playerInv.setItemStack(null);
		}
	}

	private void mergePlayerStackIntoSlot(int clickedButton, InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		int j2 = clickedButton == 0 ? playerStack.stackSize : 1;

		if (j2 > slot.getItemStackLimit(playerStack) - slotStack.stackSize) {
			j2 = slot.getItemStackLimit(playerStack) - slotStack.stackSize;
		}

		if (j2 > playerStack.getMaxStackSize() - slotStack.stackSize) {
			j2 = playerStack.getMaxStackSize() - slotStack.stackSize;
		}

		playerStack.splitStack(j2);

		if (playerStack.stackSize == 0) {
			playerInv.setItemStack(null);
		}

		slotStack.stackSize += j2;
	}

	private void mergeSlotStackIntoPlayer(EntityPlayer player, InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		int i2 = slotStack.stackSize;

		if (i2 > 0 && i2 + playerStack.stackSize <= playerStack.getMaxStackSize()) {
			playerStack.stackSize += i2;
			slotStack = slot.decrStackSize(i2);

			if (slotStack.stackSize == 0) {
				slot.putStack(null);
			}

			slot.onPickupFromSlot(player, playerInv.getItemStack());
		}
	}

	private void swapPlayerAndSlotStacks(InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		slot.putStack(playerStack);
		playerInv.setItemStack(slotStack);
	}

	@Override
	public void onSwapPlayerStack(EntityPlayer player, int equipSlot) {
		ItemStack equipStack = player.inventory.getStackInSlot(equipSlot);

		if (equipStack != null && equipStack.stackSize <= 0) {
			equipStack = null;
			player.inventory.setInventorySlotContents(equipSlot, null);
		}

		ItemStack slotStack = slot.getStack();

		if (equipStack == null && slotStack != null) {
			if (slot.canTakeStack(player)) {
				player.inventory.setInventorySlotContents(equipSlot, slotStack);
				slot.putStack(null);
				slot.onPickupFromSlot(player, slotStack);
			}
		} else if (equipStack != null && slotStack == null) {
			if (slot.isItemValid(equipStack)) {
				int k1 = slot.getItemStackLimit(equipStack);

				if (equipStack.stackSize > k1) {
					slot.putStack(equipStack.splitStack(k1));
				} else {
					slot.putStack(equipStack);
					player.inventory.setInventorySlotContents(equipSlot, null);
				}
			}
		} else if (equipStack != null) {
			if (slot.canTakeStack(player) && slot.isItemValid(equipStack)) {
				int mergeSize = slot.getItemStackLimit(equipStack);

				if (equipStack.stackSize > mergeSize) {
					slot.putStack(equipStack.splitStack(mergeSize));
					slot.onPickupFromSlot(player, slotStack);

					if (!player.inventory.addItemStackToInventory(slotStack)) {
						player.dropItem(slotStack, true);
					}
				} else {
					slot.putStack(equipStack);
					player.inventory.setInventorySlotContents(equipSlot, slotStack);
					slot.onPickupFromSlot(player, slotStack);
				}
			}
		}
	}

	@Override
	public void onClone(EntityPlayer player) {
		if (!player.capabilities.isCreativeMode || player.inventory.getItemStack() != null)
			return;
		if (slot.getHasStack()) {
			if (slot.getStack().stackSize > 0) {
				ItemStack stack = slot.getStack().copy();
				stack.stackSize = stack.getMaxStackSize();
				player.inventory.setItemStack(stack);
			} else {
				slot.putStack(null);
			}
		}
	}

	@Override
	public void onThrow(EntityPlayer player, int clickedButton) {
		if (player.inventory.getItemStack() != null)
			return;
		if (slot.getHasStack() && slot.canTakeStack(player)) {
			ItemStack stack = slot.decrStackSize(clickedButton == 0 ? 1 : slot.getStack().stackSize);
			slot.onPickupFromSlot(player, stack);
			player.dropItem(stack, true);
		}
	}

	@Override
	public void onPickUpAll(ContainerBase container, EntityPlayer player, int clickedButton) {
		ItemStack playerStack = player.inventory.getItemStack();

		if (playerStack != null && (!slot.getHasStack() || !slot.canTakeStack(player))) {
			int startSlot = clickedButton == 0 ? 0 : container.inventorySlots.size() - 1;
			int step = clickedButton == 0 ? 1 : -1;

			for (int i3 = 0; i3 < 2; ++i3) {
				for (int slot = startSlot; slot >= 0 && slot < container.inventorySlots.size() &&
						playerStack.stackSize < playerStack.getMaxStackSize(); slot += step) {
					Slot otherSlot = container.inventorySlots.get(slot);

					if (otherSlot.getHasStack() && Container.canAddItemToSlot(otherSlot, playerStack, true) &&
							otherSlot.canTakeStack(player) &&
							container.canMergeSlot(playerStack, otherSlot) &&
							(i3 != 0 ||
									otherSlot.getStack().stackSize !=
											otherSlot.getStack().getMaxStackSize())) {
						int l = Math.min(playerStack.getMaxStackSize() - playerStack.stackSize,
								otherSlot.getStack().stackSize);
						ItemStack itemstack2 = otherSlot.decrStackSize(l);
						playerStack.stackSize += l;

						if (itemstack2.stackSize <= 0) {
							otherSlot.putStack(null);
						}

						otherSlot.onPickupFromSlot(player, itemstack2);
					}
				}
			}
		}

		container.detectAndSendChanges();
	}

	@Override
	public boolean canDragIntoSlot() {
		return true;
	}

	@Override
	public boolean canTransferFromSlot() {
		return slot.getStack() != null && slot.getStack().stackSize > 0;
	}

	@Override
	public boolean transferFrom(IAdvancedSlot srcSlot) {
		ItemStack srcStack = srcSlot.getImmutableStack();
		ItemStack stack = slot.getStack();
		if (stack != null) {
			if (srcStack != null && ItemStackUtil.areStacksCombinable(stack, srcStack)) {
				int j = srcSlot.extract(stack.getMaxStackSize() - stack.stackSize).stackSize;
				stack.stackSize += j;
				return true;
			}
		} else {
			if (slot.isItemValid(srcStack)) // Forge: Make sure to respect isItemValid in the slot.
			{
				slot.putStack(srcSlot.extract(srcStack.getMaxStackSize()));
				slot.onSlotChanged();
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack getImmutableStack() {
		return slot.getStack();
	}

	@Override
	public ItemStack extract(int size) {
		ItemStack stack = getImmutableStack().splitStack(size);
		if (slot.getStack().stackSize <= 0)
			slot.putStack(null);
		else
			slot.onSlotChanged();
		return stack;
	}
}
