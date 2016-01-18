package com.hea3ven.tools.commonutils.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class WorldHelper {

	public static <T extends TileEntity> T getTile(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te == null)
			return null;
		try {
			return (T) te;
		} catch (ClassCastException e) {
			return null;
		}
	}
}
