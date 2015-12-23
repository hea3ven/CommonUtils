package com.hea3ven.tools.commonutils.mod;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.ItemBlock;

class InfoBlockVariant extends InfoBlock {

	private IProperty variantProp;
	private String variantSuffix;
	private Map<Object, Integer> variantMetas;

	public InfoBlockVariant(Block block, String domain, String name,
			Class<? extends ItemBlock> itemCls, Object[] itemArgs, IProperty variantProp,
			String variantSuffix, Map<Object, Integer> variantMetas) {
		super(block, domain, name, itemCls, itemArgs);
		this.variantProp = variantProp;
		this.variantSuffix = variantSuffix;
		this.variantMetas = variantMetas;
	}

	public InfoBlockVariant(Block block, String domain, String name,
			Class<? extends ItemBlock> itemCls, IProperty variantProp, String variantSuffix,
			Map<Object, Integer> variantMetas) {
		this(block, domain, name, itemCls, null, variantProp, variantSuffix, variantMetas);
	}

	public InfoBlockVariant(Block block, String domain, String name, IProperty variantProp,
			String variantSuffix, Map<Object, Integer> variantMetas) {
		super(block, domain, name, ItemBlock.class, null);
		this.variantProp = variantProp;
		this.variantSuffix = variantSuffix;
		this.variantMetas = variantMetas;
	}

	public IProperty getVariantProp() {
		return variantProp;
	}

	public String getVariantSuffix() {
		return variantSuffix;
	}

	public int getMeta(Object variant) {
		return variantMetas.get(variant);
	}
}
