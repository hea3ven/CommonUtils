package com.hea3ven.tools.commonutils.mod.info;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemInfo {

    private final Identifier id;
    private final Item item;

    public ItemInfo(Identifier id, Item item) {
        this.id = id;
        this.item = item;
    }

    public Identifier getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }
}
