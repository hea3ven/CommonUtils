package com.hea3ven.tools.commonutils.mod;

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
		registerRecipes(proxy);
	}

	private void registerBlocks(ProxyModBase proxy) {
		for (InfoBlock block : proxy.blocks) {
			GameRegistry.registerBlock(block.getBlock(), block.getItemCls(), block.getName(),
					(block.getItemArgs() != null) ? block.getItemArgs() : new Object[0]);
		}
	}

	private void registerTileEntities(ProxyModBase proxy) {
		for (InfoTileEntity tile : proxy.tiles) {
			GameRegistry.registerTileEntity(tile.getTileClass(), tile.getName());
		}
	}

	private void registerItems(ProxyModBase proxy) {
		for (InfoItem item : proxy.items) {
			GameRegistry.registerItem(item.getItem(), item.getName());
		}
	}

	private void registerGuiHandlers(ProxyModBase proxy) {
		NetworkRegistry.INSTANCE.registerGuiHandler(proxy.getModId(), proxy.getGuiHandler());
	}

	private void registerRecipes(ProxyModBase proxy) {
		proxy.registerRecipes();
	}
}
