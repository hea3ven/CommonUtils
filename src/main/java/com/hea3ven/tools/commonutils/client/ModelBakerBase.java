package com.hea3ven.tools.commonutils.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBakerBase {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.RenderingManager");

	private DefaultStateMapper stateMap = new DefaultStateMapper();

	@SubscribeEvent
	public void onTextureStichPreEvent(TextureStitchEvent.Pre event) {
	}

	@SubscribeEvent
	public void onTextureStichPostEvent(TextureStitchEvent.Post event) {
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
	}

	protected IModel getModel(ResourceLocation modelLoc) {
		return getModel(modelLoc, null);
	}

	protected IModel getModel(ResourceLocation modelLoc, ResourceLocation fallback) {
//		try {
			return ModelLoaderRegistry.getModel(modelLoc);
//		} catch (IOException e) {
//			if (fallback != null) {
//				try {
//					return ModelLoaderRegistry.getModel(fallback);
//				} catch (IOException e2) {
//					logger.warn("Could not find model {} or fallback {}", modelLoc, fallback);
//					return ModelLoaderRegistry.getMissingModel();
//				}
//			}
//			logger.warn("Could not find model {}", modelLoc);
//			return ModelLoaderRegistry.getMissingModel();
//		}
	}

	protected IBakedModel bake(IModel model) {
		return bake(model, model.getDefaultState());
	}

	protected IBakedModel bake(IModel model, VertexFormat format) {
		return bake(model, model.getDefaultState(), format);
	}

	protected IBakedModel bake(IModel model, IModelState modelState) {
		return bake(model, modelState, Attributes.DEFAULT_BAKED_FORMAT);
	}

	protected IBakedModel bake(IModel model, IModelState modelState, VertexFormat format) {
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

	protected IBakedModel bake(IModel model, Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, model.getDefaultState(), textures);
	}

	protected IBakedModel bake(IModel model, IModelState modelState,
			Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, modelState, Attributes.DEFAULT_BAKED_FORMAT, textures);
	}

	protected IBakedModel bake(IModel model, VertexFormat format,
			Map<ResourceLocation, TextureAtlasSprite> textures) {
		return bake(model, model.getDefaultState(), format, textures);
	}

	protected IBakedModel bake(IModel model, IModelState modelState, VertexFormat format,
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

	protected IModel retexture(HashMap<String, String> textures, IModel blockModel) {
		if (blockModel instanceof IRetexturableModel)
			return ((IRetexturableModel) blockModel).retexture(ImmutableMap.copyOf(textures));
		else if (blockModel instanceof MultiModel) {
			Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
			for (Entry<String, Pair<IModel, IModelState>> entry : ((MultiModel) blockModel).getParts()
					.entrySet()) {
				parts.put(entry.getKey(), Pair.of(retexture(textures, entry.getValue().getLeft()),
						entry.getValue().getRight()));
			}
			return new MultiModel(retexture(textures, ((MultiModel) blockModel).getBaseModel()),
					blockModel.getDefaultState(), parts);
		}
		return blockModel;
	}

	protected String getPropertyString(IBlockState state) {
		return stateMap.getPropertyString(state.getProperties());
	}
}
