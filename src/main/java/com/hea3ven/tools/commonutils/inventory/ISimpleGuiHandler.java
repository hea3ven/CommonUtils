package com.hea3ven.tools.commonutils.inventory;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ISimpleGuiHandler {

	Container createContainer(EntityPlayer player, World world, BlockPos pos);

	Gui createGui(EntityPlayer player, World world, BlockPos pos);
}
