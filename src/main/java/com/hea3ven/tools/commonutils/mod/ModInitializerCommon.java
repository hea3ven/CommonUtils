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
		registerItems(proxy);
	}

	public void onInitEvent(ProxyModBase proxy) {
	}

	public void onPostInitEvent(ProxyModBase proxy) {
		registerGuiHandlers(proxy);
	}

	private void registerBlocks(ProxyModBase proxy) {
		for (InfoBlock block : proxy.getBlocks()) {
			GameRegistry.registerBlock(block.getBlock(), block.getItemCls(), block.getName(),
					(block.getItemArgs() != null) ? block.getItemArgs() : new Object[0]);
		}
		for (InfoBlockVariant item : proxy.getVariantBlocks()) {
			GameRegistry.registerBlock(item.getBlock(), item.getItemCls(), item.getName(),
					(item.getItemArgs() != null) ? item.getItemArgs() : new Object[0]);
		}
	}

	private void registerTileEntities(ProxyModBase proxy) {
		for (InfoTileEntity tile : proxy.getTileEntities()) {
			GameRegistry.registerTileEntity(tile.getTileClass(), tile.getName());
		}
	}

	private void registerItems(ProxyModBase proxy) {
		for (InfoItem item : proxy.getItems()) {
			GameRegistry.registerItem(item.getItem(), item.getName());
		}
	}

	private void registerGuiHandlers(ProxyModBase proxy) {
		for (Pair<String, IGuiHandler> guiHandler : proxy.getGuiHandlers()) {
			NetworkRegistry.INSTANCE.registerGuiHandler(guiHandler.getKey(), guiHandler.getValue());
		}
	}
}
