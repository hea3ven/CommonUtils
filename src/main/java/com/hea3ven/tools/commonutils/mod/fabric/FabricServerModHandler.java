package com.hea3ven.tools.commonutils.mod.fabric;

import java.lang.reflect.Proxy;
import java.util.Map;

import net.minecraft.client.gui.screen.Screens;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import com.hea3ven.tools.commonutils.mod.Mod;
import com.hea3ven.tools.commonutils.mod.ScreenFactory;
import com.hea3ven.tools.commonutils.util.ReflectionUtil;

public class FabricServerModHandler {

    static public void onInitializeServer(Mod mod) {
        mod.onPreInit();
        FabricModHandler.onInitialize(mod);
        mod.onInit();
        mod.onPostInit();
    }

    @SuppressWarnings("unchecked")
    private static void registerScreenFactory(ContainerType containerType, ScreenFactory factory) {
        Class<?> factoryIface =
                ReflectionUtil.findNestedClass(Screens.class, Class::isInterface);

        Object screenFactory =
                ReflectionUtil.newInstance(ContainerType.class, new Class[] {factoryIface},
                        new Object[] {createScreenFactory(factoryIface, factory)});

        ReflectionUtil.reflectField(Screens.class, "PROVIDERS", "field_17409",
                field -> {
                    Map<ContainerType, Object> screenFactories =
                            (Map<ContainerType, Object>) field.get(null);
                    screenFactories.put(containerType, screenFactory);
                });
    }

    @SuppressWarnings("unchecked")
    private static Object createScreenFactory(Class<?> factoryIface, ScreenFactory factory) {
        return Proxy.newProxyInstance(factoryIface.getClassLoader(), new Class[] {factoryIface},
                (proxy, method, args) -> factory.create((Container) args[0],
                        (PlayerInventory) args[1], (Text) args[2]));
    }
}
