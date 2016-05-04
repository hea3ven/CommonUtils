package com.hea3ven.tools.commonutils.client;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelUVLock;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;

public class BakerUtil {
	public static IBakedModel bake(IModel model) {
		return bake(model, model.getDefaultState());
	}

	public static IBakedModel bake(IModel model, VertexFormat format) {
		return bake(model, model.getDefaultState(), format);
	}

	public static IBakedModel bake(IModel model, IModelState modelState) {
		return bake(model, modelState, Attributes.DEFAULT_BAKED_FORMAT);
	}

	public static IBakedModel bake(IModel model, IModelState modelState, VertexFormat format) {
		return model.bake(modelState, format, location -> Minecraft.getMinecraft()
				.getTextureMapBlocks()
				.getAtlasSprite(location.toString()));
	}

	public static IBakedModel bake(IModel model, Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, model.getDefaultState(), textures);
	}

	public static IBakedModel bake(IModel model, IModelState modelState,
			Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, modelState, Attributes.DEFAULT_BAKED_FORMAT, textures);
	}

	public static IBakedModel bake(IModel model, VertexFormat format,
			Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, model.getDefaultState(), format, textures);
	}

	public static IBakedModel bake(IModel model, IModelState modelState, VertexFormat format,
			final Map<ResourceLocation, TextureAtlasSprite> textures) {
		if (!textures.containsKey(new ResourceLocation("missingno")))
			textures.put(new ResourceLocation("missingno"),
					Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
		if (!textures.containsKey(new ResourceLocation("builtin/white")))
			textures.put(new ResourceLocation("builtin/white"),
					Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
		return model.bake(modelState, format, textures::get);
	}

	public static IModel retexture(Map<String, String> textures, IModel blockModel) {
		if (blockModel instanceof IRetexturableModel)
			return ((IRetexturableModel) blockModel).retexture(ImmutableMap.copyOf(textures));
//		else if (blockModel instanceof MultiModel) {
//			Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
//			for (Entry<String, Pair<IModel, IModelState>> entry : ((MultiModel) blockModel).getParts()
//					.entrySet()) {
//				parts.put(entry.getKey(), Pair.of(retexture(textures, entry.getValue().getLeft()),
//						entry.getValue().getRight()));
//			}
//			return new MultiModel(retexture(textures, ((MultiModel) blockModel).getBaseModel()),
//					blockModel.getDefaultState(), parts);
//		}
		return blockModel;
	}

	public static IModel uvlock(IModel model, boolean value) {
		if (model instanceof IModelUVLock)
			model = ((IModelUVLock) model).uvlock(value);
		return model;
	}
}
