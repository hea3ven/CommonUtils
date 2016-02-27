package com.hea3ven.tools.commonutils.mod.config;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public class FileConfigManagerBuilder implements ConfigManagerBuilder {
	private String name;
	private String desc;
	private String fileName;
	private List<CategoryConfigManagerBuilder> categories = new ArrayList<>();
	private Consumer<Configuration> updater;

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

	public CategoryConfigManagerBuilder addCategory(String catName) {
		return new CategoryConfigManagerBuilder(this, catName);
	}

	private FileConfigManagerBuilder endCategory(CategoryConfigManagerBuilder categoryBuilder) {
		categories.add(categoryBuilder);
		return this;
	}

	@Override
	public ConfigManager build(String modId, Path path) {
		Path filePath = path.resolve(fileName);
		Configuration conf = new Configuration(filePath.toFile());
		Map<Property, Consumer<Property>> propListeners = new HashMap<>();
		for (CategoryConfigManagerBuilder ctyBuilder : categories) {
			ConfigCategory category = conf.getCategory(ctyBuilder.name);
			ctyBuilder.build(modId, propListeners, category);
		}
		if (updater != null)
			updater.accept(conf);
		return new FileConfigManager(modId, name, desc, conf, propListeners);
	}

	public ConfigManagerBuilder Update(Consumer<Configuration> updater) {
		this.updater = updater;
		return this;
	}

	public static class CategoryConfigManagerBuilder implements ConfigManagerBuilder {

		private final FileConfigManagerBuilder parent;
		private CategoryConfigManagerBuilder parentCat;
		private final String name;
		private List<CategoryConfigManagerBuilder> subCategories = new ArrayList<>();
		private List<ValueBuilder> values = new ArrayList<>();

		public CategoryConfigManagerBuilder(String name) {
			parent = null;
			parentCat = null;
			this.name = name;
		}

		public CategoryConfigManagerBuilder(CategoryConfigManagerBuilder parentCat, String name) {
			parent = null;
			this.parentCat = parentCat;
			this.name = name;
		}

		public CategoryConfigManagerBuilder(FileConfigManagerBuilder parent, String name) {
			this.parent = parent;
			parentCat = null;
			this.name = name;
		}

		public CategoryConfigManagerBuilder addValue(String name, String defaultValue, Property.Type type,
				String desc, Consumer<Property> listener) {
			return addValue(name, defaultValue, type, desc, listener, false, false);
		}

		public CategoryConfigManagerBuilder addValue(String name, String defaultValue, Property.Type type,
				String desc, Consumer<Property> listener, boolean requiresMcRestart,
				boolean requiresWorldRestart) {
			values.add(new ValueBuilder(name, defaultValue, type, desc, listener, requiresMcRestart,
					requiresWorldRestart));
			return this;
		}

		public CategoryConfigManagerBuilder addSubCategory(String name) {
			return new CategoryConfigManagerBuilder(this, name);
		}

		public CategoryConfigManagerBuilder add(CategoryConfigManagerBuilder subCat) {
			if (subCat == null)
				return this;

			subCat.parentCat = this;
			subCategories.add(subCat);
			return this;
		}

		public FileConfigManagerBuilder endCategory() {
			return parent.endCategory(this);
		}

		public CategoryConfigManagerBuilder endSubCategory() {
			return parentCat.endCategory(this);
		}

		public CategoryConfigManagerBuilder endCategory(CategoryConfigManagerBuilder child) {
			subCategories.add(child);
			return this;
		}

		@Nullable
		@Override
		public ConfigManager build(String modId, Path path) {
			return null;
		}

		public void build(String modId, Map<Property, Consumer<Property>> propListeners,
				ConfigCategory category) {
			category.setLanguageKey(getLanguageKey(modId));
			for (ValueBuilder valBuilder : values) {
				Property prop = new Property(valBuilder.name, valBuilder.defaultValue, valBuilder.type,
						getLanguageKey(modId) + "." + valBuilder.name).setRequiresMcRestart(
						valBuilder.requiresMcRestart)
						.setRequiresWorldRestart(valBuilder.requiresWorldRestart);
				category.put(valBuilder.name, prop);
				prop.comment = valBuilder.desc;
				propListeners.put(prop, valBuilder.listener);
			}
			for (CategoryConfigManagerBuilder subCat : subCategories) {
				subCat.build(modId, propListeners, new ConfigCategory(subCat.name, category));
			}
		}

		private String getLanguageKey(String modId) {
			if (parent != null)
				return modId + ".config." + name;
			else
				return parentCat.getLanguageKey(modId) + "." + name;
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
