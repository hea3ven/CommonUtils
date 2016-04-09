package com.hea3ven.tools.commonutils.mod;

import net.minecraft.enchantment.Enchantment;

public class InfoEnchantment {
	private final Enchantment enchantment;
	private final String domain;
	private final String name;

	public InfoEnchantment(Enchantment enchantment, String domain, String name) {
		this.enchantment = enchantment;
		this.domain = domain;
		this.name = name;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getLocalizationName() {
		String locName = name;
		int index;
		while ((index = locName.indexOf('_')) != -1) {
			locName = locName.substring(0, index) + Character.toUpperCase(locName.charAt(index + 1)) +
					locName.substring(index + 2);
		}
		return locName;
	}
}
