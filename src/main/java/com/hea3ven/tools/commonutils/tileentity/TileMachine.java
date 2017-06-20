package com.hea3ven.tools.commonutils.tileentity;

import javax.annotation.Nonnull;

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
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (this.hasCustomName())
			compound.setString("CustomName", this.customName);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		customName = compound.getString("CustomName");
	}
}
