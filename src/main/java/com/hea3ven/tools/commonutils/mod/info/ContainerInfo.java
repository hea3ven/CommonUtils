package com.hea3ven.tools.commonutils.mod.info;

import java.util.function.BiFunction;

import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;

public class ContainerInfo {
    private final Identifier id;
    private final BiFunction<Integer, PlayerInventory, Object> factory;
    private ContainerType type;

    public ContainerInfo(Identifier id, BiFunction<Integer, PlayerInventory, Object> factory) {
        this.id = id;
        this.factory = factory;
    }

    public Identifier getId() {
        return id;
    }

    public BiFunction<Integer, PlayerInventory, Object> getFactory() {
        return factory;
    }

    public ContainerType<?> getType() {
        return type;
    }

    public void setType(ContainerType type) {
        this.type = type;
    }
}
