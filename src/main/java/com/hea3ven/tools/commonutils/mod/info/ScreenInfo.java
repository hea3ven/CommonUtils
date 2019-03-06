package com.hea3ven.tools.commonutils.mod.info;

import net.minecraft.util.Identifier;

import com.hea3ven.tools.commonutils.mod.ScreenFactory;

public class ScreenInfo {

    private final Identifier id;
    private final ScreenFactory factory;

    public ScreenInfo(Identifier id, ScreenFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    public Identifier getId() {
        return id;
    }

    public ScreenFactory getFactory() {
        return factory;
    }
}
