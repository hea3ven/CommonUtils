package com.hea3ven.tools.commonutils.util;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockUtil {
	public static boolean placeBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			IBlockState newState) {
		AxisAlignedBB box = newState.getBlock().getCollisionBoundingBox(newState, world, pos).offset(pos);
		if (world.checkNoEntityCollision(box)) {

			if (!world.setBlockState(pos, newState, 3))
				return false;

			IBlockState state = world.getBlockState(pos);
			state.getBlock().onBlockPlacedBy(world, pos, state, player, stack);

			SoundType soundtype = newState.getBlock().getSoundType();
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
					(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			if (stack != null && !player.isCreative())
				--stack.stackSize;
			return true;
		}
		return false;
	}
}
