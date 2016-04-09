package com.hea3ven.tools.commonutils.mod;

import net.minecraft.item.Item;

class InfoItem {

	private Item item;
	private String domain;
	private String name;
	private String[] variants;

	public InfoItem(Item item, String domain, String name, String[] variants) {
		this(item, domain, name);
		this.variants = variants;
	}

	public InfoItem(Item item, String domain, String name) {
		this.item = item;
		this.domain = domain;
		this.name = name;
	}

	public Item getItem() {
		return item;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String[] getVariants() {
		return variants;
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
