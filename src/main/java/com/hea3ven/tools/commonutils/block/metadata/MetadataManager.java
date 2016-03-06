package com.hea3ven.tools.commonutils.block.metadata;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

public class MetadataManager {
	private final Block block;
	private List<MetadataPart> parts = new ArrayList<>();

	public MetadataManager(Block block, List<MetadataPart> parts) {
		this.block = block;
		this.parts = parts;
	}

	public int getMeta(IBlockState state) {
		int meta = 0;
		for (MetadataPart part : parts) {
			meta = part.setMeta(state, meta);
		}
		return meta;
	}

	public IBlockState getState(int meta) {
		IBlockState state = block.getDefaultState();
		for (MetadataPart part : parts) {
			state = part.setValue(state, meta);
		}
		return state;
	}

	static class MetadataPart<T extends Comparable<T>> {
		private final byte offset;
		private final byte size;
		private final IProperty<T> prop;
		private final byte mask;
		private final List<T> values;

		public MetadataPart(byte offset, byte size, IProperty<T> prop) {
			this.prop = prop;
			this.offset = offset;
			this.size = size;
			mask = (byte) (Math.pow(2, size) - 1);
			values = new ArrayList<>(prop.getAllowedValues());
		}

		public IBlockState setValue(IBlockState state, int meta) {
			int i = (meta >> offset) & mask;
			return state.withProperty(prop, values.get(i));
		}

		public int setMeta(IBlockState state, int meta) {
			return meta | values.indexOf(state.getValue(prop)) << offset;
		}
	}
}
