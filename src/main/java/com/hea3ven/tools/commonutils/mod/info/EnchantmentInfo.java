package com.hea3ven.tools.commonutils.mod.info;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;

public class EnchantmentInfo {
    private final Identifier id;
    private final Enchantment enchantment;

    public EnchantmentInfo(Identifier id, Enchantment enchantment) {
        this.id = id;
        this.enchantment = enchantment;
    }

    public Identifier getId() {
        return id;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }
}
