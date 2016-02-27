package com.hea3ven.tools.commonutils.mod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.base.Throwables;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FileConfigManager extends ConfigManager {
	private final Configuration conf;
	private final Map<Property, Consumer<Property>> propListeners;

	public FileConfigManager(String modId, String name, String desc, Configuration conf,
			Map<Property, Consumer<Property>> propListeners) {
		super(modId, name, desc);
		this.conf = conf;
		this.propListeners = propListeners;
	}

	@Override
	public List<IConfigElement> getConfigElements() {
		List<IConfigElement> elems = new ArrayList<>();
		for (String catName : conf.getCategoryNames()) {
			if (catName.contains("."))
				continue;
			elems.add(new ConfigElement(conf.getCategory(catName)));
		}
		return elems;
	}

	@Override
	protected void reload(boolean initial) {
		for (Entry<Property, Consumer<Property>> entry : propListeners.entrySet()) {
			if (initial || entry.getKey().hasChanged())
				entry.getValue().accept(entry.getKey());
		}

		if (conf.hasChanged()) {
			conf.save();

			try {
				ReflectionHelper.findMethod(Configuration.class, conf, new String[] {"resetChangedState"})
						.invoke(conf);
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		}
	}
}
