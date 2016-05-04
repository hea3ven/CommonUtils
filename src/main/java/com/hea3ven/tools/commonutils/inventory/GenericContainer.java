package com.hea3ven.tools.commonutils.inventory;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

public class GenericContainer extends ContainerBase {

	private IUpdateHandler updateHandler = null;
	private int[] valuesCache;

	private List<SlotType> slotsTypes = Lists.newArrayList();
	private int playerSlotsStart = -1;

	public GenericContainer addInputOutputSlots(IInventory inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.INPUT_OUTPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotInput.class, inv);
		return this;
	}

	public GenericContainer addInputOutputSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.INPUT_OUTPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotItemHandlerBase.class, inv);
		return this;
	}

	public GenericContainer addInputSlots(IInventory inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.INPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotInput.class, inv);
		return this;
	}

	public GenericContainer addInputSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.INPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotItemHandlerBase.class, inv);
		return this;
	}

	public GenericContainer addOutputSlots(IInventory inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.OUTPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotOutput.class, inv);
		return this;
	}

	public GenericContainer addOutputSlots(IItemHandler inv, int slotOff, int xOff, int yOff, int xSize,
			int ySize) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(SlotType.OUTPUT);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, SlotItemHandlerOutput.class, inv);
		return this;
	}

	public GenericContainer addSlots(int slotOff, int xOff, int yOff, int xSize, int ySize,
			Class<? extends Slot> slotCls, Object... args) {
		return addSlots(SlotType.INPUT, slotOff, xOff, yOff, xSize, ySize, slotCls, args);
	}

	public GenericContainer addSlots(SlotType slotType, int slotOff, int xOff, int yOff, int xSize, int ySize,
			Class<? extends Slot> slotCls, Object... args) {
		for (int i = 0; i < xSize * ySize; i++)
			slotsTypes.add(slotType);
		addInventoryGrid(slotOff, xOff, yOff, xSize, ySize, slotCls, args);
		return this;
	}

	public GenericContainer addPlayerSlots(InventoryPlayer playerInv) {
		return addPlayerSlots(playerInv, null);
	}

	public GenericContainer addPlayerSlots(final InventoryPlayer playerInv, final Set<Integer> lockedSlots) {
		playerSlotsStart = slotsTypes.size();
		for (int i = 0; i < 9 * 4; i++)
			slotsTypes.add(SlotType.PLAYER);
		addInventoryGrid(playerInv, 9, 8, 84, 9, 3);
		addInventoryGrid(0, 8, 142, 9, 1, (slot, x, y) -> {
			if (lockedSlots != null && lockedSlots.contains(slot))
				return new SlotLocked(playerInv, slot, x, y);
			else
				return new Slot(playerInv, slot, x, y);
		});
		return this;
	}

	public GenericContainer setUpdateHandler(final IInventory inv) {
		return setUpdateHandler(new IUpdateHandler() {
			@Override
			public int getFieldCount() {
				return inv.getFieldCount();
			}

			@Override
			public void setField(int id, int data) {
				inv.setField(id, data);
			}

			@Override
			public int getField(int id) {
				return inv.getField(id);
			}
		});
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
				for (ICrafting crafting : listeners) {
					crafting.sendProgressBarUpdate(this, i, updateHandler.getField(i));
				}
			}
		} else {
			for (int i = 0; i < valuesCache.length; i++) {
				if (valuesCache[i] != updateHandler.getField(i)) {
					for (ICrafting crafting : listeners) {
						crafting.sendProgressBarUpdate(this, i, updateHandler.getField(i));
					}
					valuesCache[i] = updateHandler.getField(i);
				}
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack originalStack = null;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			originalStack = stack.copy();

			SlotType slotType = slotsTypes.get(index);

			if (slotType == SlotType.INPUT || slotType == SlotType.OUTPUT ||
					slotType == SlotType.INPUT_OUTPUT) {
				if (!this.mergeItemStack(stack, playerSlotsStart, playerSlotsStart + 9 * 4, true)) {
					return null;
				}
				slot.onSlotChange(stack, originalStack);
			} else if (slotType == SlotType.DISPLAY) {
				if (slot instanceof SlotCustom) {
					stack = ((SlotCustom) slot).provideItemStack();
					if (!this.mergeItemStack(stack, playerSlotsStart, playerSlotsStart + 9 * 4, true)) {
						return null;
					}
					slot.onSlotChange(stack, originalStack);
				}
				return null;
			} else if (playerSlotsStart <= index && index < playerSlotsStart + 9 * 4) {
				boolean success = false;
				for (int i = 0; i < slotsTypes.size(); i++) {
					SlotType dstSlotType = slotsTypes.get(i);
					if (dstSlotType == SlotType.INPUT || dstSlotType == SlotType.INPUT_OUTPUT) {
						if (this.mergeItemStack(stack, i, i + 1, false)) {
							success = true;
							break;
						}
					} else if (dstSlotType == SlotType.DISPLAY) {
						Slot dstSlot = getSlot(i);
						if (dstSlot instanceof SlotCustom) {
							if (((SlotCustom) dstSlot).receiveItemStack(stack)) {
								success = true;
								break;
							}
						}
					}
				}
				if (!success)
					return null;
			}

			if (stack.stackSize == 0)
				slot.putStack(null);
			else {
				slot.onSlotChanged();
			}

			if (stack.stackSize == originalStack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stack);
		}

		return originalStack;
	}

	public int getPlayerSlotsStart() {
		return playerSlotsStart;
	}

	public enum SlotType {
		INPUT, OUTPUT, DISPLAY, INPUT_OUTPUT, PLAYER
	}
}
