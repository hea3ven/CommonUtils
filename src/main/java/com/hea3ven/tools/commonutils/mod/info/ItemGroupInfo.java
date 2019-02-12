package com.hea3ven.tools.commonutils.mod.info;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class ItemGroupInfo {

    private final Identifier id;
    private final ItemGroup group;

    public ItemGroupInfo(Identifier id, ItemGroup group) {
        this.id = id;
        this.group = group;
    }

    public Identifier getId() {
        return id;
    }

    public ItemGroup getGroup() {
        return group;
    }
}
