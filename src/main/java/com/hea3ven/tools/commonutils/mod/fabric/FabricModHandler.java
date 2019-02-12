package com.hea3ven.tools.commonutils.mod.fabric;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;

import net.minecraft.util.registry.Registry;

import com.hea3ven.tools.commonutils.mod.info.BlockInfo;
import com.hea3ven.tools.commonutils.mod.info.EnchantmentInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemInfo;
import com.hea3ven.tools.commonutils.mod.Mod;
import com.hea3ven.tools.commonutils.mod.info.ContainerInfo;

public class FabricModHandler {

    static public void onInitialize(Mod mod) {
        mod.onPreInit();
        for (BlockInfo blockInfo : mod.getBlocks().values()) {
            Registry.register(Registry.BLOCK, blockInfo.getId(), blockInfo.getBlock());
            Registry.register(Registry.ITEM, blockInfo.getId(), blockInfo.getItem());
            // TODO: check if it's needed
            // blockInfo.getItem().registerBlockItemMap(Item.BLOCK_ITEM_MAP, blockInfo.getItem());
            if (blockInfo.getBlockEntityType() != null) {
                Registry.register(Registry.BLOCK_ENTITY, blockInfo.getId(),
                        blockInfo.getBlockEntityType());
            }
        }
        for (ContainerInfo containerInfo : mod.getContainers().values()) {
            ContainerProviderRegistry.INSTANCE.registerFactory(containerInfo.getId(),
                    containerInfo.getFactory());
        }
        for (ItemInfo itemInfo : mod.getItems().values()) {
            Registry.register(Registry.ITEM, itemInfo.getId(), itemInfo.getItem());
        }
        for (EnchantmentInfo enchantmentInfo : mod.getEnchantments().values()) {
            Registry.register(Registry.ENCHANTMENT, enchantmentInfo.getId(),
                    enchantmentInfo.getEnchantment());
        }
        mod.onInit();
        mod.onPostInit();
    }
}