package com.hea3ven.tools.commonutils.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlaceParams {
	public final BlockPos pos;
	public final EnumFacing side;
	public final Vec3d hit;
	public final double depth;

	public PlaceParams(BlockPos pos, EnumFacing side, double hitX, double hitY, double hitZ) {
		depth = ((hitX * 2 - 1) * side.getFrontOffsetX() + (hitY * 2 - 1) * side.getFrontOffsetY() +
				(hitZ * 2 - 1) * side.getFrontOffsetZ());
		this.side = side;
		if (depth < 1) {
			this.pos = pos;
			hit = new Vec3d(hitX, hitY, hitZ);
		} else {
			this.pos = pos.offset(side);
			hit = new Vec3d(hitX - side.getFrontOffsetX(), hitY - side.getFrontOffsetY(),
					hitZ - side.getFrontOffsetZ());
		}
	}
}
