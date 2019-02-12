package com.hea3ven.tools.commonutils.mod.info;

import net.fabricmc.fabric.api.container.ContainerFactory;

import net.minecraft.util.Identifier;

public class ContainerInfo {
    private final Identifier id;
    private final ContainerFactory factory;

    public ContainerInfo(Identifier id, ContainerFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    public Identifier getId() {
        return id;
    }

    public ContainerFactory getFactory() {
       return factory;
    }
}
