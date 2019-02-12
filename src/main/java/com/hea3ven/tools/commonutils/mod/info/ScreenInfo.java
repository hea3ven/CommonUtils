package com.hea3ven.tools.commonutils.mod.info;

import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;

import net.minecraft.util.Identifier;

public class ScreenInfo {

    private final Identifier id;
    private final ContainerScreenFactory factory;

    public ScreenInfo(Identifier id, ContainerScreenFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    public Identifier getId() {
        return id;
    }

    public ContainerScreenFactory getFactory() {
        return factory;
    }
}
