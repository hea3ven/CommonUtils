package com.hea3ven.tools.commonutils.inventory;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;

public class GenericGuiHandler implements IGuiHandler {
	private Map<Integer, ISimpleGuiHandler> guis = Maps.newHashMap();

	public void addGui(int id, ISimpleGuiHandler handler) {
		guis.put(id, handler);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return guis.get(ID).createContainer(player, world, new BlockPos(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return guis.get(ID).createGui(player, world, new BlockPos(x, y, z));
	}
}
