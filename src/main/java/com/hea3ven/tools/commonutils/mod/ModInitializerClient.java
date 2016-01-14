package com.hea3ven.tools.commonutils.mod;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.hea3ven.tools.commonutils.client.renderer.SimpleItemMeshDefinition;

public class ModInitializerClient extends ModInitializerCommon {

	@Override
	public void onPreInitEvent(ProxyModBase proxy, FMLPreInitializationEvent event) {
		super.onPreInitEvent(proxy, event);
		registerVariantBlocks(proxy);
		registerVariantItems(proxy);
		registerBlocksItemModels(proxy);
	}

	@Override
	public void onInitEvent(ProxyModBase proxy) {
		super.onInitEvent(proxy);
		registerKeyBindings(proxy);
	}

	@Override
	public void onPostInitEvent(ProxyModBase proxy) {
		super.onPostInitEvent(proxy);
	}

	private void registerVariantBlocks(ProxyModBase proxy) {
		for (InfoBlock block : proxy.blocks) {
			if (!(block instanceof InfoBlockVariant))
				continue;
			InfoBlockVariant blockVar = (InfoBlockVariant) block;
			List<String> variants = Lists.newArrayList();
			for (Object metalObj : blockVar.getVariantProp().getAllowedValues()) {
				IStringSerializable value = (IStringSerializable) metalObj;
				String name = block.getDomain() + ":" + value.getName() + blockVar.getVariantSuffix();
				variants.add(name);
			}
			ModelBakery.addVariantName(Item.getItemFromBlock(block.getBlock()),
					variants.toArray(new String[variants.size()]));

			ModelLoader.setCustomStateMapper(block.getBlock(),
					(new StateMap.Builder()).withName(blockVar.getVariantProp())
							.withSuffix(blockVar.getVariantSuffix())
							.build());
		}
	}

	private void registerVariantItems(ProxyModBase proxy) {
		for (InfoItem item : proxy.items) {
			if (item.getVariants() == null)
				continue;
			for (String variant : item.getVariants()) {
				ModelLoader.addVariantName(item.getItem(), item.getDomain() + ":" + variant);
			}
		}
	}

	private void registerBlocksItemModels(ProxyModBase proxy) {
		for (InfoBlock block : proxy.blocks) {
			if (block instanceof InfoBlockVariant) {
				InfoBlockVariant blockVar = (InfoBlockVariant) block;
				for (Object valueObj : blockVar.getVariantProp().getAllowedValues()) {
					IStringSerializable value = (IStringSerializable) valueObj;
					String name = block.getDomain() + ":" + value.getName() + blockVar.getVariantSuffix();
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block.getBlock()),
							blockVar.getMeta(value), new ModelResourceLocation(name, "inventory"));
				}
			} else {
				ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(block.getBlock()),
						new SimpleItemMeshDefinition(block.getDomain() + ":" + block.getName()));
			}
		}
		for (InfoItem item : proxy.items) {
			if (item.getVariants() == null) {
				ModelLoader.setCustomMeshDefinition(item.getItem(),
						new SimpleItemMeshDefinition(item.getDomain() + ":" + item.getName()));
			} else {
				int i = 0;
				for (String variant : item.getVariants()) {
					ModelLoader.setCustomModelResourceLocation(item.getItem(), i++,
							new ModelResourceLocation(item.getDomain() + ":" + variant, "inventory"));
				}
			}
		}
	}

	private void registerKeyBindings(ProxyModBase proxy) {
		proxy.registerKeyBindings();
		if (proxy.keyBindingManager != null) {
			MinecraftForge.EVENT_BUS.register(proxy.keyBindingManager);
			for (KeyBinding keyBinding : proxy.keyBindingManager.getKeyBindings()) {
				ClientRegistry.registerKeyBinding(keyBinding);
			}
		}
	}
}
