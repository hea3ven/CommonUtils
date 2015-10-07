package com.hea3ven.tools.commonutils.mod;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.fml.common.network.IGuiHandler;

public class ProxyModBase {

	private ModInitializerCommon modInitializer;

	public ProxyModBase(ModInitializerCommon modInitializer) {
		this.modInitializer = modInitializer;
	}

	public final void onPreInitEvent() {
		modInitializer.onPreInitEvent(this);
	}

	public final void onInitEvent() {
		modInitializer.onInitEvent(this);
	}

	public final void onPostInitEvent() {
		modInitializer.onPostInitEvent(this);
	}

	public void registerEnchantments() {
	}

	public void registerBlocks() {
	}

	public ArrayList<Pair<String, IGuiHandler>> getGuiHandlers() {
		return Lists.newArrayList();
	}

	public ArrayList<Pair<String, Integer>> getBlockItems() {
		return Lists.newArrayList();
	}

}
