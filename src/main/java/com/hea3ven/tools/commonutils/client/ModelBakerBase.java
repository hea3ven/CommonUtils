package com.hea3ven.tools.commonutils.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
		try {
			return ModelLoaderRegistry.getModel(modelLoc);
		} catch (Exception e) {
			if (fallback != null) {
				try {
					return ModelLoaderRegistry.getModel(fallback);
				} catch (Exception e2) {
					logger.warn("Could not find model {} or fallback {}", modelLoc, fallback);
					return ModelLoaderRegistry.getMissingModel();
				}
			}
			logger.warn("Could not find model {}", modelLoc);
			return ModelLoaderRegistry.getMissingModel();
		}
	}

	protected String getPropertyString(IBlockState state) {
		return stateMap.getPropertyString(state.getProperties());
	}
}
