package com.hea3ven.tools.commonutils.util;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class PlayerUtil {
	public static HeldEquipment getHeldEquipment(EntityPlayer player, Item item) {
		for (EnumHand hand : EnumHand.values()) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack != null && stack.getItem() == item)
				return new HeldEquipment(player, hand);
		}
		return null;
	}

	public static class HeldEquipment {
		@Nonnull
		public EntityPlayer player;
		@Nonnull
		public EnumHand hand;

		public HeldEquipment(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
			this.player = player;
			this.hand = hand;
		}

		public ItemStack getStack() {
			return player.getHeldItem(hand);
		}
	}
}
