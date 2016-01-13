package com.hea3ven.tools.commonutils.mod.config;

import java.util.List;

import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class ConfigManager {

	private final String modId;
	private final String name;
	private final String desc;

	public ConfigManager(String modId, String name, String desc) {
		this.modId = modId;
		this.name = name;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public abstract List<IConfigElement> getConfigElements();

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event == null || event.modID.equals(modId)) {
			// Reload config
			reload(event == null);
		}
	}

	protected abstract void reload(boolean initial);
}
