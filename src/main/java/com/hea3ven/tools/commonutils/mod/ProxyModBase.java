package com.hea3ven.tools.commonutils.mod;

import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.fml.common.network.IGuiHandler;

public class ProxyModBase {

	private ModInitializerCommon modInitializer;

	public ProxyModBase(ModInitializerCommon modInitializer) {
		this.modInitializer = modInitializer;
	}

	public void onPreInitEvent() {
		modInitializer.onPreInitEvent(this);
	}

	public void onInitEvent() {
		modInitializer.onInitEvent(this);
	}

	public void onPostInitEvent() {
		modInitializer.onPostInitEvent(this);
	}

	public void registerEnchantments() {
	}

	public List<Pair<String, IGuiHandler>> getGuiHandlers() {
		return Lists.newArrayList();
	}

	public List<InfoBlock> getBlocks() {
		return Lists.newArrayList();
	}

	public List<InfoBlockVariant> getVariantBlocks() {
		return Lists.newArrayList();
	}

	public List<InfoTileEntity> getTileEntities() {
		return Lists.newArrayList();
	}

	public List<InfoItem> getItems() {
		return Lists.newArrayList();
	}

}
