package com.hea3ven.tools.commonutils.mod;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class InfoBlock {

	private Block block;
	private String domain;
	private String name;
	private Class<? extends ItemBlock> itemCls;

	public InfoBlock(Block block, String domain, String name, Class<? extends ItemBlock> itemCls) {
		this.block = block;
		this.domain = domain;
		this.name = name;
		this.itemCls = itemCls;
	}

	public InfoBlock(Block block, String domain, String name) {
		this(block, domain, name, ItemBlock.class);
	}

	public Block getBlock() {
		return block;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public Class<? extends ItemBlock> getItemCls() {
		return itemCls;
	}

}
