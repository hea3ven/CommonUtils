package com.hea3ven.tools.commonutils.mod;

import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ModInitializerCommon {

	public void onPreInitEvent(ProxyModBase proxy) {
		proxy.registerEnchantments();
		registerBlocks(proxy);
		registerTileEntities(proxy);
		registerItems(proxy);
		registerCreativeTabs(proxy);
	}

	public void onInitEvent(ProxyModBase proxy) {
		registerNetworkPackets(proxy);
		registerRecipes(proxy);
		registerGuiHandlers(proxy);
	}

	public void onPostInitEvent(ProxyModBase proxy) {
	}

	private void registerBlocks(ProxyModBase proxy) {
		proxy.registerBlocks();
		for (InfoBlock block : proxy.blocks) {
			GameRegistry.registerBlock(block.getBlock(), block.getItemCls(), block.getName(),
					(block.getItemArgs() != null) ? block.getItemArgs() : new Object[0]);
		}
	}

	private void registerTileEntities(ProxyModBase proxy) {
		proxy.registerTileEntities();
		for (InfoTileEntity tile : proxy.tiles) {
			GameRegistry.registerTileEntity(tile.getTileClass(), tile.getName());
		}
	}

	private void registerItems(ProxyModBase proxy) {
		proxy.registerItems();
		for (InfoItem item : proxy.items) {
			GameRegistry.registerItem(item.getItem(), item.getName());
		}
	}

	private void registerCreativeTabs(ProxyModBase proxy) {
		proxy.registerCreativeTabs();
	}

	private void registerNetworkPackets(ProxyModBase proxy) {
		proxy.registerNetworkPackets();
	}

	private void registerRecipes(ProxyModBase proxy) {
		proxy.registerRecipes();
		for (IRecipe recipe : proxy.recipes) {
			GameRegistry.addRecipe(recipe);
		}
	}

	private void registerGuiHandlers(ProxyModBase proxy) {
		proxy.registerGuis();
		NetworkRegistry.INSTANCE.registerGuiHandler(proxy.getModId(), proxy.getGuiHandler());
	}
}
