package com.hea3ven.tools.commonutils.client.model;

import java.util.HashMap;
import java.util.List;

import com.google.common.base.Optional;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class SmartModelCached extends DelegatedSmartModel {

	private HashMap<Integer, SmartModelCached> cache;

	public SmartModelCached() {
		super(null);
		cache = new HashMap<>();
	}

	public SmartModelCached(IBakedModel delegate) {
		super(delegate);
	}

	public void put(IBlockState state, IBakedModel model) {
		cache.put(calculateHash((IExtendedBlockState) state), new SmartModelCached(model));
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		SmartModelCached model = cache.get(calculateHash((IExtendedBlockState) state));
		if (model != null)
			return model.getQuads(state, side, rand);
		else
			return super.getQuads(state, side, rand);
	}

//	@Override
//	public IBakedModel handleBlockState(IBlockState state) {
//		SmartModelCached model = cache.get(calculateHash((IExtendedBlockState) state));
//		if (model != null)
//			return model;
//		else
//			return this;
//	}

	private int calculateHash(IExtendedBlockState state) {
		HashCodeBuilder hash = new HashCodeBuilder();
		for (Comparable value : state.getProperties().values())
			hash.append(value);
		for (Optional<?> value : state.getUnlistedProperties().values())
			hash.append(value);
		return hash.build();
	}
}
