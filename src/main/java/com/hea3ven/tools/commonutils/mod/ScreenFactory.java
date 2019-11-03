package com.hea3ven.tools.commonutils.mod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ContainerProvider;
import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public interface ScreenFactory<T extends Container, U extends Screen & ContainerProvider<T>> {
    U create(T container, PlayerInventory playerInv, Text name);
}
