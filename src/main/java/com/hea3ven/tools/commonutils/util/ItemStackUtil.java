package com.hea3ven.tools.commonutils.util;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemStackUtil {
	private static final Random RANDOM = new Random();

	public static void dropFromBlock(World world, BlockPos pos, ItemStack stack) {
		if (world.isRemote)
			return;

		float xOff = RANDOM.nextFloat() * 0.8F + 0.1F;
		float yOff = RANDOM.nextFloat() * 0.8F + 0.1F;
		float zOff = RANDOM.nextFloat() * 0.8F + 0.1F;

		EntityItem entityitem = new EntityItem(world, pos.getX() + (double) xOff,
				pos.getY() + (double) yOff, pos.getZ() + (double) zOff, stack.copy());

		if (stack.hasTagCompound()) {
			entityitem
					.getEntityItem()
					.setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
		}

		entityitem.motionX = RANDOM.nextGaussian() * 0.05d;
		entityitem.motionY = RANDOM.nextGaussian() * 0.05d + 0.20000000298023224d;
		entityitem.motionZ = RANDOM.nextGaussian() * 0.05d;
		world.spawnEntityInWorld(entityitem);
	}

	public static boolean areItemsCompletelyEqual(ItemStack stackA, ItemStack stackB) {
		return ItemStack.areItemsEqual(stackA, stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static boolean areStacksCombinable(ItemStack stackA, ItemStack stackB) {
		return stackA.getItem() == stackB.getItem() &&
				(!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) &&
				ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static EnumActionResult useItem(World world, EntityPlayer player, ItemStack stack, BlockPos pos,
			EnumHand hand, EnumFacing facing) {
		EnumActionResult result =
				stack.getItem().onItemUseFirst(stack, player, world, pos, facing, 0.5F, 0.5F, 0.5F, hand);

		if (result == EnumActionResult.PASS) {
			result = stack.getItem().onItemUse(stack, player, world, pos, hand, facing, 0.5F, 0.5F, 0.5F);
		}
		return result;
	}
}
