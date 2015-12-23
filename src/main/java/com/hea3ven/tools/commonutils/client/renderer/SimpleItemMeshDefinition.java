package com.hea3ven.tools.commonutils.client.renderer;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public class SimpleItemMeshDefinition implements ItemMeshDefinition {
	private final ModelResourceLocation modelLocation;

	public SimpleItemMeshDefinition(String modelLocation) {
		this(modelLocation.contains("#") ? new ModelResourceLocation(modelLocation) :
				new ModelResourceLocation(modelLocation, "inventory"));
	}

	public SimpleItemMeshDefinition(ModelResourceLocation modelLocation) {
		this.modelLocation = modelLocation;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return modelLocation;
	}
}
