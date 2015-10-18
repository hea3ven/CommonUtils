package com.hea3ven.tools.commonutils.mod;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class InfoBlock {

	private Block block;
	private String domain;
	private String name;
	private Class<? extends ItemBlock> itemCls;
	private Object[] itemArgs;

	public InfoBlock(Block block, String domain, String name, Class<? extends ItemBlock> itemCls,
			Object[] itemArgs) {
		this.block = block;
		this.domain = domain;
		this.name = name;
		this.itemCls = itemCls;
		this.itemArgs = itemArgs;
	}

	public InfoBlock(Block block, String domain, String name, Class<? extends ItemBlock> itemCls) {
		this(block, domain, name, itemCls, null);
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

	public Object[] getItemArgs() {
		return itemArgs;
	}

}
