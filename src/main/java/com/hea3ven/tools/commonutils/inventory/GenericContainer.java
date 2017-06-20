package com.hea3ven.tools.commonutils.inventory;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public class GenericContainer extends ContainerBase {

	private IUpdateHandler updateHandler = null;
	private int[] valuesCache;

	private int playerSlotsStart = -1;

	public GenericContainer addGenericSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		super.addSlots(inv, slotOff, xOff, yOff, xSize, ySize);
		return this;
	}

	public GenericContainer addGenericSlots(int xOff, int yOff, int xSize, int ySize,
			SlotSupplier slotSupplier) {
		super.addSlots(xOff, yOff, xSize, ySize, slotSupplier);
		return this;
	}

	public GenericContainer addInputSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		addSlots(xOff, yOff, xSize, ySize,
				(slot, x, y) -> new SlotItemHandlerBase(inv, slotOff + slot, x, y));
		return this;
	}

	public GenericContainer addOutputSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		addSlots(xOff, yOff, xSize, ySize,
				(slot, x, y) -> new SlotOutput(inv, slotOff + slot, x, y));
		return this;
	}

	public GenericContainer addPlayerSlots(InventoryPlayer playerInv) {
		return addPlayerSlots(playerInv, Collections.emptySet());
	}

	public GenericContainer addPlayerSlots(final InventoryPlayer playerInv, final Set<Integer> lockedSlots) {
		playerSlotsStart = inventorySlots.size();
		CombinedInvWrapper playerItemHandler =
				new CombinedInvWrapper(new PlayerMainInvWrapper(playerInv), new PlayerOffhandInvWrapper(playerInv));
		addSlots(playerItemHandler, 9, 8, 84, 9, 3);
		addSlots(8, 142, 9, 1, (slot, x, y) -> {
			if (lockedSlots.contains(slot))
				return new SlotLocked(playerItemHandler, slot, x, y);
			else
				return new SlotItemHandlerBase(playerItemHandler, slot, x, y);
		});
		return this;
	}

	public GenericContainer setUpdateHandler(IUpdateHandler updateHandler) {
		this.updateHandler = updateHandler;
		valuesCache = null;
		return this;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public void updateProgressBar(int id, int data) {
		if (updateHandler != null)
			updateHandler.setField(id, data);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		if (updateHandler == null)
			return;

		if (valuesCache == null) {
			valuesCache = new int[updateHandler.getFieldCount()];
			for (int i = 0; i < updateHandler.getFieldCount(); i++) {
				valuesCache[i] = updateHandler.getField(i);
				for (IContainerListener crafting : listeners) {
					crafting.sendWindowProperty(this, i, updateHandler.getField(i));
				}
			}
		} else {
			for (int i = 0; i < valuesCache.length; i++) {
				if (valuesCache[i] != updateHandler.getField(i)) {
					for (IContainerListener crafting : listeners) {
						crafting.sendWindowProperty(this, i, updateHandler.getField(i));
					}
					valuesCache[i] = updateHandler.getField(i);
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		IAdvancedSlot slot = getAdvancedSlot(index);
		if (slot != null && slot.canTransferFromSlot()) {
			if (playerSlotsStart <= index && index < playerSlotsStart + 9 * 4) {
				if (!mergeSlot(slot, 0, playerSlotsStart, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!mergeSlot(slot, playerSlotsStart, playerSlotsStart + 9 * 4, true)) {
					return ItemStack.EMPTY;
				}
			}
		}

		return ItemStack.EMPTY;
	}

	public int getPlayerSlotsStart() {
		return playerSlotsStart;
	}
}
