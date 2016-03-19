package com.hea3ven.tools.commonutils.client;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IRetexturableModel;

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
		Function<ResourceLocation, TextureAtlasSprite> textureGetter =
				new Function<ResourceLocation, TextureAtlasSprite>() {
					public TextureAtlasSprite apply(ResourceLocation location) {
						return Minecraft.getMinecraft()
								.getTextureMapBlocks()
								.getAtlasSprite(location.toString());
					}
				};
		return model.bake(modelState, format, textureGetter);
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
		Function<ResourceLocation, TextureAtlasSprite> textureGetter =
				new Function<ResourceLocation, TextureAtlasSprite>() {
					public TextureAtlasSprite apply(ResourceLocation location) {
						return textures.get(location);
					}
				};
		return model.bake(modelState, format, textureGetter);
	}

	public static IModel retexture(HashMap<String, String> textures, IModel blockModel) {
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
}