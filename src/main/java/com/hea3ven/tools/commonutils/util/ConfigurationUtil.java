package com.hea3ven.tools.commonutils.util;

import net.minecraftforge.common.config.ConfigCategory;

public class ConfigurationUtil {
	public static ConfigCategory getSubCategory(ConfigCategory cat, String name) {
		for (ConfigCategory subCat : cat.getChildren()) {
			if (subCat.getName().equals(name))
				return subCat;
		}
		return null;
	}
}
