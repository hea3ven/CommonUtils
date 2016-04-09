package com.hea3ven.tools.commonutils.mod;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

class InfoBlock {

	private Block block;
	private String name;
	private ItemBlock item;

	public InfoBlock(Block block, String name, ItemBlock item) {
		this.block = block;
		this.name = name;
		this.item= item;
	}

	public Block getBlock() {
		return block;
	}

	public String getName() {
		return name;
	}

	public ItemBlock getItem() {
		return item;
	}

	public String getLocalizationName() {
		String locName = name;
		int index;
		while ((index = locName.indexOf('_')) != -1) {
			locName = locName.substring(0, index) + Character.toUpperCase(locName.charAt(index + 1)) +
					locName.substring(index + 2);
		}
		return locName;
	}
}
