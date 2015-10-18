package com.hea3ven.tools.commonutils.mod;

import net.minecraft.item.Item;

public class InfoItem {

	private Item item;
	private String domain;
	private String name;

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

}
