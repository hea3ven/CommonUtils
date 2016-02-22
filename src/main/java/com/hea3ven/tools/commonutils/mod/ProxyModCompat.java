package com.hea3ven.tools.commonutils.mod;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyModCompat extends ProxyModBase {

	private final String compatModId;

	public ProxyModCompat(String modId, String compatModId) {
		super(modId);
		this.compatModId = compatModId;
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		if (Loader.isModLoaded(compatModId))
			super.onPreInitEvent(event);
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		if (Loader.isModLoaded(compatModId))
			super.onInitEvent(event);
	}

	@Override
	public void onPostInitEvent(FMLPostInitializationEvent event) {
		if (Loader.isModLoaded(compatModId))
			super.onPostInitEvent(event);
	}
}
