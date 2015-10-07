package com.hea3ven.tools.commonutils.mod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public abstract class ModInitializerCommon {

	public void onPreInitEvent(ProxyModBase proxy) {
		proxy.registerEnchantments();
		proxy.registerBlocks();
	}

	public void onInitEvent(ProxyModBase proxy) {
	}

	public void onPostInitEvent(ProxyModBase proxy) {
		registerGuiHandlers(proxy);
	}

	private void registerGuiHandlers(ProxyModBase proxy) {
		for (Pair<String, IGuiHandler> guiHandler : proxy.getGuiHandlers()) {
			NetworkRegistry.INSTANCE.registerGuiHandler(guiHandler.getKey(), guiHandler.getValue());
		}
	}
}
