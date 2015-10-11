package com.hea3ven.tools.commonutils.mod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ModInitializerCommon {

	public void onPreInitEvent(ProxyModBase proxy) {
		proxy.registerEnchantments();
		registerBlocks(proxy);
		registerTileEntities(proxy);
	}

	public void onInitEvent(ProxyModBase proxy) {
	}

	public void onPostInitEvent(ProxyModBase proxy) {
		registerGuiHandlers(proxy);
	}

	private void registerBlocks(ProxyModBase proxy) {
		for (InfoBlock item : proxy.getBlocks()) {
			GameRegistry.registerBlock(item.getBlock(), item.getItemCls(), item.getName());
		}
		for (InfoBlockVariant item : proxy.getVariantBlocks()) {
			GameRegistry.registerBlock(item.getBlock(), item.getItemCls(), item.getName());
		}
	}

	private void registerTileEntities(ProxyModBase proxy) {
		for (InfoTileEntity item : proxy.getTileEntities()) {
			GameRegistry.registerTileEntity(item.getTileClass(), item.getName());
		}
	}

	private void registerGuiHandlers(ProxyModBase proxy) {
		for (Pair<String, IGuiHandler> guiHandler : proxy.getGuiHandlers()) {
			NetworkRegistry.INSTANCE.registerGuiHandler(guiHandler.getKey(), guiHandler.getValue());
		}
	}
}
