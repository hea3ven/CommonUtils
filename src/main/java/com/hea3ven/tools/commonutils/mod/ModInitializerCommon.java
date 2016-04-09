package com.hea3ven.tools.commonutils.mod;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.tools.commonutils.mod.config.ConfigManagerBuilder;

public abstract class ModInitializerCommon {

	public void onPreInitEvent(final ProxyModBase proxy, FMLPreInitializationEvent event) {
		registerConfig(proxy, event);
		registerEnchantments(proxy);
		registerBlocks(proxy);
		registerTileEntities(proxy);
		registerItems(proxy);
		registerCreativeTabs(proxy);
	}

	public void onInitEvent(ProxyModBase proxy) {
		registerNetworkPackets(proxy);
		registerRecipes(proxy);
		registerGuiHandlers(proxy);
		registerCommands(proxy);
	}

	public void onPostInitEvent(ProxyModBase proxy) {
	}

	private void registerConfig(ProxyModBase proxy, FMLPreInitializationEvent event) {
		proxy.registerConfig();
		ConfigManagerBuilder builder = proxy.cfgMgrBuilder;
		if (builder == null)
			return;
		Path configDir = Paths.get(event.getModConfigurationDirectory().toString());
		proxy.cfgMgr = builder.build(proxy.getModId(), configDir);
		if (proxy.cfgMgr != null) {
			MinecraftForge.EVENT_BUS.register(proxy.cfgMgr);
			proxy.cfgMgr.onConfigChanged(null);
		}
	}

	private void registerEnchantments(ProxyModBase proxy) {
		proxy.registerEnchantments();
		for (InfoEnchantment ench : proxy.enchantments) {
			ench.getEnchantment().setName(proxy.getModId() + "." + ench.getLocalizationName());
			ench.getEnchantment().setRegistryName(proxy.getModId() + ":" + ench.getName());
			GameRegistry.register(ench.getEnchantment());
		}
	}

	private void registerBlocks(ProxyModBase proxy) {
		proxy.registerBlocks();
		for (InfoBlock block : proxy.blocks) {
			block.getBlock().setUnlocalizedName(proxy.getModId() + "." + block.getLocalizationName());
			block.getBlock().setRegistryName(proxy.getModId() + ":" + block.getName());
			GameRegistry.register(block.getBlock());
			if (block.getItem() != null) {
				block.getItem().setUnlocalizedName(proxy.getModId() + "." + block.getLocalizationName());
				block.getItem().setRegistryName(proxy.getModId() + ":" + block.getName());
				GameRegistry.register(block.getItem());
			}
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
			item.getItem().setUnlocalizedName(proxy.getModId() + "." + item.getLocalizationName());
			item.getItem().setRegistryName(proxy.getModId() + ":" + item.getName());
			GameRegistry.register(item.getItem());
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

	private void registerCommands(ProxyModBase proxy) {
		proxy.registerCommands();
	}
}
