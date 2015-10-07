package com.hea3ven.tools.commonutils.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileMachine extends TileEntity {

	private String customName = null;

	public boolean hasCustomName() {
		return customName != null;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getCustomName() {
		return customName;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (this.hasCustomName())
			compound.setString("CustomName", this.customName);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		customName = compound.getString("CustomName");
	}
}
