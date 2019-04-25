package com.hea3ven.tools.commonutils.mod.fabric;

import java.lang.reflect.Proxy;
import java.util.function.BiFunction;

import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import com.hea3ven.tools.commonutils.mod.Mod;
import com.hea3ven.tools.commonutils.mod.info.BlockInfo;
import com.hea3ven.tools.commonutils.mod.info.ContainerInfo;
import com.hea3ven.tools.commonutils.mod.info.EnchantmentInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemInfo;
import com.hea3ven.tools.commonutils.util.ReflectionUtil;

class FabricModHandler {

    static void onInitialize(Mod mod) {
        for (BlockInfo blockInfo : mod.getBlocks().values()) {
            Registry.register(Registry.BLOCK, blockInfo.getId(), blockInfo.getBlock());
            Registry.register(Registry.ITEM, blockInfo.getId(), blockInfo.getItem());
            // TODO: check if it's needed
            blockInfo.getItem().registerBlockItemMap(Item.BLOCK_ITEM_MAP, blockInfo.getItem());
            if (blockInfo.getBlockEntityTypeBuilder() != null) {
                blockInfo.setBlockEntityType(
                        Registry.register(Registry.BLOCK_ENTITY, blockInfo.getId(),
                                blockInfo.getBlockEntityTypeBuilder().build(null)));
            }
        }
        for (ContainerInfo containerInfo : mod.getContainers().values()) {
            containerInfo.setType(createContainerType(containerInfo.getFactory()));
            Registry.register(Registry.CONTAINER, containerInfo.getId(), containerInfo.getType());
            //            ContainerProviderRegistry.INSTANCE.registerFactory(containerInfo.getId(),
            //                    containerInfo.getFactory());
        }
        for (ItemInfo itemInfo : mod.getItems().values()) {
            Registry.register(Registry.ITEM, itemInfo.getId(), itemInfo.getItem());
        }
        for (EnchantmentInfo enchantmentInfo : mod.getEnchantments().values()) {
            Registry.register(Registry.ENCHANTMENT, enchantmentInfo.getId(),
                    enchantmentInfo.getEnchantment());
        }
    }

    private static ContainerType createContainerType(
            BiFunction<Integer, PlayerInventory, Object> factory) {
        Class<?> factoryIface =
                ReflectionUtil.findNestedClass(ContainerType.class, Class::isInterface);

        return ReflectionUtil.newInstance(ContainerType.class, new Class[] {factoryIface},
                new Object[] {createFactory(factoryIface, factory)});
    }

    private static Object createFactory(Class<?> factoryIface,
            BiFunction<Integer, PlayerInventory, Object> factory) {
        return Proxy.newProxyInstance(factoryIface.getClassLoader(), new Class[] {factoryIface},
                (proxy, method, args) -> factory.apply((Integer) args[0],
                        (PlayerInventory) args[1]));
    }
}
