package com.hea3ven.tools.commonutils.mod;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiConfigInternal extends GuiConfig {
	public GuiConfigInternal(GuiScreen parentScreen, ProxyModBase proxy) {
		super(parentScreen, proxy.getConfigManager().getConfigElements(), proxy.getModId(), false, false,
				proxy.getConfigManager().getDesc());
	}
}
