package com.hea3ven.tools.commonutils.mod;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

import net.minecraftforge.client.model.ModelLoader;

public class ModInitializerClient extends ModInitializerCommon {

	@Override
	public void onPreInitEvent(ProxyModBase proxy) {
		super.onPreInitEvent(proxy);
		registerVariantBlocks(proxy);
	}

	@Override
	public void onInitEvent(ProxyModBase proxy) {
		super.onInitEvent(proxy);

	}

	@Override
	public void onPostInitEvent(ProxyModBase proxy) {
		super.onPostInitEvent(proxy);

		registerBlocksItemModels(proxy);
	}

	private void registerVariantBlocks(ProxyModBase proxy) {
		for (InfoBlockVariant item : proxy.getVariantBlocks()) {
			List<String> variants = Lists.newArrayList();
			for (Object metalObj : item.getVariantProp().getAllowedValues()) {
				IStringSerializable value = (IStringSerializable) metalObj;
				String name = item.getDomain() + ":" + value.getName() + item.getVariantSuffix();
				variants.add(name);
			}
			ModelBakery.addVariantName(Item.getItemFromBlock(item.getBlock()),
					variants.toArray(new String[variants.size()]));

			ModelLoader.setCustomStateMapper(item.getBlock(),
					(new StateMap.Builder())
							.setProperty(item.getVariantProp())
							.setBuilderSuffix(item.getVariantSuffix())
							.build());
		}
	}

	private void registerBlocksItemModels(ProxyModBase proxy) {
		ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (InfoBlock item : proxy.getBlocks()) {
			for (int i = 0; i < 16; i++) {
				itemMesher.register(Item.getItemFromBlock(item.getBlock()), i,
						new ModelResourceLocation(item.getDomain() + ":" + item.getName(),
								"inventory"));
			}
		}
		for (InfoBlockVariant item : proxy.getVariantBlocks()) {
			for (Object valueObj : item.getVariantProp().getAllowedValues()) {
				IStringSerializable value = (IStringSerializable) valueObj;
				String name = item.getDomain() + ":" + value.getName() + item.getVariantSuffix();
				itemMesher.register(Item.getItemFromBlock(item.getBlock()), item.getMeta(value),
						new ModelResourceLocation(name, "inventory"));
			}
		}
		for (InfoItem item : proxy.getItems()) {
			for (int i = 0; i < 16; i++) {
				itemMesher.register(item.getItem(), i, new ModelResourceLocation(
						item.getDomain() + ":" + item.getName(), "inventory"));
			}
		}
	}
}
