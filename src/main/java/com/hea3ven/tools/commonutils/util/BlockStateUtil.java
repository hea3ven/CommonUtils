package com.hea3ven.tools.commonutils.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;

public class BlockStateUtil {

	public static BlockStateContainer addProperties(Block block, BlockStateContainer parentBlockState, IProperty[] properties) {
		List<IProperty> newProperties = new ArrayList<>(parentBlockState.getProperties());
		Collections.addAll(newProperties, properties);
		return new BlockStateContainer(block, newProperties.toArray(new IProperty[newProperties.size()]));
	}
}
