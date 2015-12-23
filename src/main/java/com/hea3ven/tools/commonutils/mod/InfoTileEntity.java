package com.hea3ven.tools.commonutils.mod;

import net.minecraft.tileentity.TileEntity;

class InfoTileEntity {

	private Class<? extends TileEntity> tileClass;
	private String name;

	public InfoTileEntity(Class<? extends TileEntity> tileClass, String name) {
		this.tileClass = tileClass;
		this.name = name;
	}

	public Class<? extends TileEntity> getTileClass() {
		return tileClass;
	}

	public String getName() {
		return name;
	}

}
