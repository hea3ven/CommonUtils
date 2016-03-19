package com.hea3ven.tools.commonutils.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Optional;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockStateUtil {

	public static BlockStateContainer addProperties(Block block, BlockStateContainer parentBlockState,
			IProperty<?>[] properties) {
		List<IProperty<?>> newProperties = new ArrayList<>(parentBlockState.getProperties());
		Collections.addAll(newProperties, properties);
		return new BlockStateContainer(block, newProperties.toArray(new IProperty[newProperties.size()]));
	}

	public static int getHashCode(IBlockState state) {
		if(state instanceof IExtendedBlockState)
			return getHashCode((IExtendedBlockState)state);
		return state.hashCode();
	}
	public static int getHashCode(IExtendedBlockState state) {
		HashCodeBuilder hash = new HashCodeBuilder();
		for (Comparable value : state.getProperties().values())
			hash.append(value);
		for (Optional<?> value : state.getUnlistedProperties().values())
			hash.append(value);
		return hash.build();
	}
}
