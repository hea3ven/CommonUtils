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
}
