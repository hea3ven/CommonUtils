package com.hea3ven.tools.commonutils.mod;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.tools.commonutils.mod.config.ConfigManagerBuilder;
import com.hea3ven.tools.commonutils.util.SidedCall;

public abstract class ModInitializerCommon {

	public void onPreInitEvent(final ProxyModBase proxy, FMLPreInitializationEvent event) {
		registerConfig(proxy, event);
		proxy.registerEnchantments();
		registerBlocks(proxy);
		registerTileEntities(proxy);
		registerItems(proxy);
		registerCreativeTabs(proxy);
		SidedCall.run(Side.CLIENT, new Runnable() {
			@Override
			public void run() {
				registerModelBakers(proxy);
			}
		});
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
		MinecraftForge.EVENT_BUS.register(proxy.cfgMgr);
		proxy.cfgMgr.onConfigChanged(null);
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

	@SideOnly(Side.CLIENT)
	private void registerModelBakers(ProxyModBase proxy) {
		proxy.registerModelBakers();
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
