package com.hea3ven.tools.commonutils.client.model;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;



@SuppressWarnings("deprecation")
public class DelegatedSmartModel implements IBakedModel {

	private IBakedModel delegate;

	public DelegatedSmartModel(IBakedModel delegate) {
		this.delegate = delegate;
	}

	public IBakedModel getDelegate() {
		return delegate;
	}

	public void setDelegate(IBakedModel delegate) {
		this.delegate = delegate;
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side) {
		return delegate.getFaceQuads(side);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return delegate.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return delegate.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return delegate.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return delegate.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return delegate.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return delegate.getItemCameraTransforms();
	}
}