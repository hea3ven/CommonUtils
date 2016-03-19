package com.hea3ven.tools.commonutils.client.model;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public abstract class SmartModelCached<T> extends DelegatedSmartModel {

	private HashMap<T, IBakedModel> cache;

	public SmartModelCached() {
		super(null);
		cache = new HashMap<>();
	}

	public SmartModelCached(IBakedModel delegate) {
		super(delegate);
	}

	public void put(T key, IBakedModel model) {
		cache.put(key, model);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (cache == null)
			return super.getQuads(state, side, rand);

		IBakedModel model = cache.get(getKey(state));
		if (model != null)
			return model.getQuads(state, side, rand);
		else
			return super.getQuads(state, side, rand);
	}

	protected abstract T getKey(IBlockState state);
}
