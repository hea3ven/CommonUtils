package com.hea3ven.tools.commonutils.block.metadata;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

import com.hea3ven.tools.commonutils.block.metadata.MetadataManager.MetadataPart;

public class MetadataManagerBuilder {

	private Block block;
	byte offset = 0;
	private List<MetadataPart> parts = new ArrayList<>();

	public MetadataManagerBuilder(Block block) {
		this.block = block;
	}

	public<T extends Comparable<T>> MetadataManagerBuilder map(byte size, IProperty<T> prop) {
		parts.add(new MetadataManager.MetadataPart<>(offset, size, prop));
		offset += size;
		return this;
	}

	public MetadataManager build() {
		return new MetadataManager(block, parts);
	}
}
