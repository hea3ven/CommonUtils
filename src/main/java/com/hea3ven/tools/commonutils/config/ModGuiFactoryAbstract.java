package com.hea3ven.tools.commonutils.config;

import java.util.Set;

import net.minecraft.client.Minecraft;

import net.minecraftforge.fml.client.IModGuiFactory;

public abstract class ModGuiFactoryAbstract implements IModGuiFactory {
	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
}
