package com.hea3ven.tools.commonutils.block.base;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.hea3ven.tools.commonutils.tileentity.TileMachine;
import com.hea3ven.tools.commonutils.util.WorldHelper;

public abstract class BlockMachine extends BlockContainer {

	private Object guiModId;
	private int guiId;

	protected BlockMachine(Material material, Object guiModId, int guiId) {
		super(material);

		this.guiModId = guiModId;
		this.guiId = guiId;
	}

	@Override
	@Nonnull
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);

		if (stack.hasDisplayName()) {
			TileMachine te = WorldHelper.getTile(world, pos);
			if (te != null)
				te.setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileMachine te = WorldHelper.getTile(world, pos);
		if (te != null)

		{
			playerIn.openGui(guiModId, guiId, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}

		return false;
	}
}
