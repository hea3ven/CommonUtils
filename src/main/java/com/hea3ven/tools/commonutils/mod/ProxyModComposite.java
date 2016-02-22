package com.hea3ven.tools.commonutils.mod;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyModComposite extends ProxyModBase {
	private Set<ProxyModBase> children = new HashSet<>();

	public ProxyModComposite(String modId) {
		super(modId);
	}

	public void add(ProxyModBase child) {
		children.add(child);
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);

		for (ProxyModBase child : children) {
			child.onPreInitEvent(event);
		}
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		super.onInitEvent(event);

		for (ProxyModBase child : children) {
			child.onInitEvent(event);
		}
	}

	@Override
	public void onPostInitEvent(FMLPostInitializationEvent event) {
		super.onPostInitEvent(event);

		for (ProxyModBase child : children) {
			child.onPostInitEvent(event);
		}
	}
}
