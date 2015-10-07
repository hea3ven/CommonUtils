package com.hea3ven.tools.commonutils.mod;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ModInitializerClient extends ModInitializerCommon {

	@Override
	public void onPostInitEvent(ProxyModBase proxy) {
		super.onPostInitEvent(proxy);

		registerBlocksItemModels(proxy);
	}

	private void registerBlocksItemModels(ProxyModBase proxy) {
		ItemModelMesher itemMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (Pair<String, Integer> item : proxy.getBlockItems()) {
			itemMesher.register(Item.getByNameOrId(item.getKey()), item.getValue(),
					new ModelResourceLocation(item.getKey(), "inventory"));
		}
	}
}
