package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hea3ven.tools.commonutils.util.ItemStackUtil;
import com.hea3ven.tools.commonutils.util.SlotUtil;

public class AdvancedSlotWrapper implements IAdvancedSlot {
	private final Slot slot;

	public AdvancedSlotWrapper(Slot slot) {
		this.slot = slot;
	}

	@Override
	public ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton) {

		if (!slot.canTakeStack(player)) {
			return ItemStack.EMPTY;
		}

		ItemStack resultStack = ItemStack.EMPTY;
		for (ItemStack extraStack = container.transferStackInSlot(player, slot.slotNumber);
				!extraStack.isEmpty() && ItemStack.areItemsEqual(slot.getStack(), extraStack);
				extraStack = container.transferStackInSlot(player, slot.slotNumber)) {
			resultStack = extraStack.copy();
		}
		return resultStack;
	}

	@Override
	public ItemStack onPickUp(EntityPlayer player, int clickedButton) {
		ItemStack slotStack = slot.getStack();
		ItemStack playerStack = player.inventory.getItemStack();

		ItemStack resultStack = ItemStack.EMPTY;
		if (!slotStack.isEmpty()) {
			resultStack = slotStack.copy();
		}

		if (slotStack.isEmpty()) {
			putPlayerStackInSlot(clickedButton, player.inventory, playerStack);
		} else if (slot.canTakeStack(player)) {
			if (playerStack.isEmpty()) {
				putSlotStackInPlayer(player, clickedButton, player.inventory, slotStack);
			} else if (slot.isItemValid(playerStack)) {
				if (ItemStackUtil.areItemsCompletelyEqual(slotStack, playerStack)) {
					mergePlayerStackIntoSlot(clickedButton, player.inventory, slotStack, playerStack);
				} else if (playerStack.getCount() <= slot.getItemStackLimit(playerStack)) {
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
		if (!playerStack.isEmpty() && slot.isItemValid(playerStack)) {
			int putSize = clickedButton == 0 ? playerStack.getCount() : 1;

			if (putSize > slot.getItemStackLimit(playerStack)) {
				putSize = slot.getItemStackLimit(playerStack);
			}

			slot.putStack(playerStack.splitStack(putSize));
		}
	}

	private void putSlotStackInPlayer(EntityPlayer player, int clickedButton, InventoryPlayer playerInv,
			ItemStack slotStack) {
		if (!slotStack.isEmpty()) {
			int k2 =
					clickedButton == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
			playerInv.setItemStack(slot.decrStackSize(k2));

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			slot.onTake(player, playerInv.getItemStack());
		} else {
			slot.putStack(ItemStack.EMPTY);
			playerInv.setItemStack(ItemStack.EMPTY);
		}
	}

	private void mergePlayerStackIntoSlot(int clickedButton, InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		int j2 = clickedButton == 0 ? playerStack.getCount() : 1;

		if (j2 > slot.getItemStackLimit(playerStack) - slotStack.getCount()) {
			j2 = slot.getItemStackLimit(playerStack) - slotStack.getCount();
		}

		if (j2 > playerStack.getMaxStackSize() - slotStack.getCount()) {
			j2 = playerStack.getMaxStackSize() - slotStack.getCount();
		}

		playerStack.shrink(j2);
		slotStack.grow(j2);
	}

	private void mergeSlotStackIntoPlayer(EntityPlayer player, InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		int i2 = slotStack.getCount();

		if (i2 > 0 && i2 + playerStack.getCount() <= playerStack.getMaxStackSize()) {
			playerStack.grow(i2);
			slotStack = slot.decrStackSize(i2);

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}

			slot.onTake(player, playerInv.getItemStack());
		}
	}

	private void swapPlayerAndSlotStacks(InventoryPlayer playerInv, ItemStack slotStack,
			ItemStack playerStack) {
		slot.putStack(playerStack);
		playerInv.setItemStack(slotStack);
	}

	@Override
	public void onSwapPlayerStack(int clickedButton, EntityPlayer player, int equipSlot) {
		ItemStack itemstack6 = player.inventory.getStackInSlot(equipSlot);

		ItemStack itemstack10 = slot.getStack();

		if (!itemstack6.isEmpty() || !itemstack10.isEmpty()) {
			if (itemstack6.isEmpty()) {
				if (slot.canTakeStack(player)) {
					player.inventory.setInventorySlotContents(clickedButton, itemstack10);
					SlotUtil.onSwapCraft(slot, itemstack10.getCount());
					slot.putStack(ItemStack.EMPTY);
					slot.onTake(player, itemstack10);
				}
			} else if (itemstack10.isEmpty()) {
				if (slot.isItemValid(itemstack6)) {
					int l1 = slot.getItemStackLimit(itemstack6);

					if (itemstack6.getCount() > l1) {
						slot.putStack(itemstack6.splitStack(l1));
					} else {
						slot.putStack(itemstack6);
						player.inventory.setInventorySlotContents(clickedButton, ItemStack.EMPTY);
					}
				}
			} else if (slot.canTakeStack(player) && slot.isItemValid(itemstack6)) {
				int i2 = slot.getItemStackLimit(itemstack6);

				if (itemstack6.getCount() > i2) {
					slot.putStack(itemstack6.splitStack(i2));
					slot.onTake(player, itemstack10);

					if (!player.inventory.addItemStackToInventory(itemstack10)) {
						player.dropItem(itemstack10, true);
					}
				} else {
					slot.putStack(itemstack6);
					player.inventory.setInventorySlotContents(clickedButton, itemstack10);
					slot.onTake(player, itemstack10);
				}
			}
		}
//		if (itemstack6.isEmpty() && !itemstack10.isEmpty()) {
//			if (slot.canTakeStack(player)) {
//				player.inventory.setInventorySlotContents(equipSlot, itemstack10);
//				slot.putStack(ItemStack.EMPTY);
//				slot.onTake(player, itemstack10);
//			}
//		} else if (!itemstack6.isEmpty() && itemstack10.isEmpty()) {
//			if (slot.isItemValid(itemstack6)) {
//				int k1 = slot.getItemStackLimit(itemstack6);

//				slot.putStack(itemstack6.splitStack(k1));
//			}
//		} else if (!itemstack6.isEmpty()) {
//			if (slot.canTakeStack(player) && slot.isItemValid(itemstack6)) {
//				int mergeSize = slot.getItemStackLimit(itemstack6);

//				if (itemstack6.getCount() > mergeSize) {
//					slot.putStack(itemstack6.splitStack(mergeSize));
//					slot.onTake(player, itemstack10);

//					if (!player.inventory.addItemStackToInventory(itemstack10)) {
//						player.dropItem(itemstack10, true);
//					}
//				} else {
//					slot.putStack(itemstack6);
//					player.inventory.setInventorySlotContents(equipSlot, itemstack10);
//					slot.onTake(player, itemstack10);
//				}
//			}
//		}
	}

	@Override
	public void onClone(EntityPlayer player) {
		if (!player.capabilities.isCreativeMode || !player.inventory.getItemStack().isEmpty())
			return;
		if (slot.getHasStack()) {
			if (!slot.getStack().isEmpty()) {
				ItemStack stack = slot.getStack().copy();
				stack.setCount(stack.getMaxStackSize());
				player.inventory.setItemStack(stack);
			} else {
				slot.putStack(ItemStack.EMPTY);
			}
		}
	}

	@Override
	public void onThrow(EntityPlayer player, int clickedButton) {
		if (!player.inventory.getItemStack().isEmpty())
			return;
		if (slot.getHasStack() && slot.canTakeStack(player)) {
			ItemStack stack = slot.decrStackSize(clickedButton == 0 ? 1 : slot.getStack().getCount());
			slot.onTake(player, stack);
			player.dropItem(stack, true);
		}
	}

	@Override
	public void onPickUpAll(ContainerBase container, EntityPlayer player, int clickedButton) {
		ItemStack playerStack = player.inventory.getItemStack();

		if (!playerStack.isEmpty() && (!slot.getHasStack() || !slot.canTakeStack(player))) {
			int startSlot = clickedButton == 0 ? 0 : container.inventorySlots.size() - 1;
			int step = clickedButton == 0 ? 1 : -1;

			for (int i3 = 0; i3 < 2; ++i3) {
				for (int slot = startSlot; slot >= 0 && slot < container.inventorySlots.size() &&
						playerStack.getCount() < playerStack.getMaxStackSize(); slot += step) {
					Slot otherSlot = container.inventorySlots.get(slot);

					if (otherSlot.getHasStack() && Container.canAddItemToSlot(otherSlot, playerStack, true) &&
							otherSlot.canTakeStack(player) &&
							container.canMergeSlot(playerStack, otherSlot) &&
							(i3 != 0 ||
									otherSlot.getStack().getCount() !=
											otherSlot.getStack().getMaxStackSize())) {
						int l = Math.min(playerStack.getMaxStackSize() - playerStack.getCount(),
								otherSlot.getStack().getCount());
						ItemStack itemstack2 = otherSlot.decrStackSize(l);
						playerStack.grow(l);

						if (itemstack2.isEmpty()) {
							this.slot.putStack(ItemStack.EMPTY);
						}

						otherSlot.onTake(player, itemstack2);
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
		return !slot.getStack().isEmpty();
	}

	@Override
	public boolean transferFrom(IAdvancedSlot srcSlot) {
		ItemStack srcStack = srcSlot.getImmutableStack();
		ItemStack stack = slot.getStack();
		if (!stack.isEmpty()) {
			if (!srcStack.isEmpty() && ItemStackUtil.areStacksCombinable(stack, srcStack)) {
				int j = srcSlot.extract(stack.getMaxStackSize() - stack.getCount()).getCount();
				stack.grow(j);
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
		slot.onSlotChanged();
		return stack;
	}
}
