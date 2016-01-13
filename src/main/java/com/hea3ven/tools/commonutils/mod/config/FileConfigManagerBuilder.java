package com.hea3ven.tools.commonutils.mod.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public class FileConfigManagerBuilder implements ConfigManagerBuilder {
	private String name;
	private String desc;
	private String fileName;
	private List<CategoryBuilder> categories = new ArrayList<>();

	public FileConfigManagerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public FileConfigManagerBuilder setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public FileConfigManagerBuilder setDesc(String desc) {
		this.desc = desc;
		return this;
	}

	public CategoryBuilder addCategory(String catName) {
		return new CategoryBuilder(this, catName);
	}

	private FileConfigManagerBuilder endCategory(CategoryBuilder categoryBuilder) {
		categories.add(categoryBuilder);
		return this;
	}

	@Override
	public ConfigManager build(String modId, Path path) {
		Path filePath = path.resolve(fileName);
		Configuration conf = new Configuration(filePath.toFile());
		Map<Property, Consumer<Property>> propListeners = new HashMap<>();
		for (CategoryBuilder ctyBuilder : categories) {
			conf.getCategory(ctyBuilder.name).setLanguageKey(modId + ".config." + ctyBuilder.name + ".cat");
			for (ValueBuilder valBuilder : ctyBuilder.values) {
				Property prop =
						conf.get(ctyBuilder.name, valBuilder.name, valBuilder.defaultValue, valBuilder.desc,
								valBuilder.type)
								.setLanguageKey(modId + ".config." + ctyBuilder.name + "." + valBuilder.name)
								.setRequiresMcRestart(valBuilder.requiresMcRestart)
								.setRequiresWorldRestart(valBuilder.requiresWorldRestart);
				propListeners.put(prop, valBuilder.listener);
			}
		}
		return new FileConfigManager(modId, name, desc, conf, propListeners);
	}

	public static class CategoryBuilder {

		private final FileConfigManagerBuilder parent;
		private final String name;
		private List<ValueBuilder> values = new ArrayList<>();

		public CategoryBuilder(FileConfigManagerBuilder parent, String name) {
			this.parent = parent;
			this.name = name;
		}

		public CategoryBuilder addValue(String name, String defaultValue, Property.Type type, String desc,
				Consumer<Property> listener) {
			return addValue(name, defaultValue, type, desc, listener, false, false);
		}

		public CategoryBuilder addValue(String name, String defaultValue, Property.Type type, String desc,
				Consumer<Property> listener, boolean requiresMcRestart, boolean requiresWorldRestart) {
			values.add(new ValueBuilder(name, defaultValue, type, desc, listener, requiresMcRestart,
					requiresWorldRestart));
			return this;
		}

		public FileConfigManagerBuilder endCategory() {
			return parent.endCategory(this);
		}
	}

	public static class ValueBuilder {
		private final String name;
		private final String defaultValue;
		private final Type type;
		private final String desc;
		private final Consumer<Property> listener;
		private final boolean requiresMcRestart;
		private final boolean requiresWorldRestart;

		public ValueBuilder(String name, String defaultValue, Type type, String desc,
				Consumer<Property> listener, boolean requiresMcRestart, boolean requiresWorldRestart) {
			this.name = name;
			this.defaultValue = defaultValue;
			this.type = type;
			this.desc = desc;
			this.listener = listener;
			this.requiresMcRestart = requiresMcRestart;
			this.requiresWorldRestart = requiresWorldRestart;
		}
	}
}
