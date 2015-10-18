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
		for (InfoBlockVariant block : proxy.getVariantBlocks()) {
			List<String> variants = Lists.newArrayList();
			for (Object metalObj : block.getVariantProp().getAllowedValues()) {
				IStringSerializable value = (IStringSerializable) metalObj;
				String name = block.getDomain() + ":" + value.getName() + block.getVariantSuffix();
				variants.add(name);
			}
			ModelBakery.addVariantName(Item.getItemFromBlock(block.getBlock()),
					variants.toArray(new String[variants.size()]));

			ModelLoader.setCustomStateMapper(block.getBlock(),
					(new StateMap.Builder())
							.setProperty(block.getVariantProp())
							.setBuilderSuffix(block.getVariantSuffix())
							.build());
		}
	}

	private void registerBlocksItemModels(ProxyModBase proxy) {
		ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (InfoBlock block : proxy.getBlocks()) {
			for (int i = 0; i < 16; i++) {
				itemMesher.register(Item.getItemFromBlock(block.getBlock()), i,
						new ModelResourceLocation(block.getDomain() + ":" + block.getName(),
								"inventory"));
			}
		}
		for (InfoBlockVariant block : proxy.getVariantBlocks()) {
			for (Object valueObj : block.getVariantProp().getAllowedValues()) {
				IStringSerializable value = (IStringSerializable) valueObj;
				String name = block.getDomain() + ":" + value.getName() + block.getVariantSuffix();
				itemMesher.register(Item.getItemFromBlock(block.getBlock()), block.getMeta(value),
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
