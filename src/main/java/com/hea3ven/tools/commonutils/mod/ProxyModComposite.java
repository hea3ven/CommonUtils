package com.hea3ven.tools.commonutils.mod;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Throwables;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyModComposite extends ProxyModBase {
	private Map<String, ProxyModBase> children = new HashMap<>();

	public ProxyModComposite(String modId) {
		super(modId);
	}

	public void addModule(String name, String modId, String clsName) {
		if (!Loader.isModLoaded(modId))
			return;
		addModule(name, clsName);
	}

	public void addModule(String name, String clsName) {
		Class<? extends ProxyModBase> cls = null;
		try {
			cls = (Class<? extends ProxyModBase>) Loader.instance().getModClassLoader().loadClass(clsName);
		} catch (ClassNotFoundException e) {
			Throwables.propagate(e);
		}
		ProxyModBase child = null;
		try {
			child = cls.getConstructor(String.class).newInstance(getModId());
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		children.put(name, child);
	}

	public <T extends ProxyModBase> T getModule(String name) {
		return (T) children.get(name);
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);

		for (ProxyModBase child : children.values()) {
			child.onPreInitEvent(event);
		}
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		super.onInitEvent(event);

		for (ProxyModBase child : children.values()) {
			child.onInitEvent(event);
		}
	}

	@Override
	public void onPostInitEvent(FMLPostInitializationEvent event) {
		super.onPostInitEvent(event);

		for (ProxyModBase child : children.values()) {
			child.onPostInitEvent(event);
		}
	}
}
