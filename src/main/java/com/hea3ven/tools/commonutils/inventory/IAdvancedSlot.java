package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAdvancedSlot {
	ItemStack onQuickMove(ContainerBase container, EntityPlayer player, int clickedButton);

	ItemStack onPickUp(EntityPlayer player, int clickedButton);

	void onSwapPlayerStack(int clickedButton, EntityPlayer player, int equipSlot);

	void onClone(EntityPlayer player);

	void onThrow(EntityPlayer player, int clickedButton);

	void onPickUpAll(ContainerBase container, EntityPlayer player, int clickedButton);

	boolean canDragIntoSlot();

	boolean canTransferFromSlot();

	boolean transferFrom(IAdvancedSlot slot);

	/**
	 * Get an inmutable representation of the stack in the slot.
	 *
	 * @return the stack in the slot.
	 */
	ItemStack getImmutableStack();

	ItemStack extract(int size);
}
