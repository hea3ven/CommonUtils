package com.hea3ven.tools.commonutils.mod.fabric;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Map;

import net.minecraft.client.gui.screen.ContainerScreenRegistry;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import com.hea3ven.tools.commonutils.mod.Mod;
import com.hea3ven.tools.commonutils.mod.ScreenFactory;
import com.hea3ven.tools.commonutils.mod.info.ContainerInfo;
import com.hea3ven.tools.commonutils.mod.info.ScreenInfo;
import com.hea3ven.tools.commonutils.util.ReflectionUtil;

public class FabricClientModHandler {

    static public void onInitializeClient(Mod mod) {
        mod.onPreInit();
        FabricModHandler.onInitialize(mod);
        for (ScreenInfo screenInfo : mod.getScreens().values()) {
            ContainerInfo containerInfo = mod.getContainerInfo(screenInfo.getId().getPath());
            registerScreenFactory(containerInfo.getType(), screenInfo.getFactory());
        }
        mod.onInit();
        mod.onPostInit();
    }

    @SuppressWarnings("unchecked")
    private static void registerScreenFactory(ContainerType containerType, ScreenFactory factory) {
        Class<?> factoryIface =
                ReflectionUtil.findNestedClass(ContainerScreenRegistry.class, Class::isInterface);

        Object screenFactory = createScreenFactory(factoryIface, factory);

        ReflectionUtil.reflectField(ContainerScreenRegistry.class, "GUI_FACTORIES", "field_17409",
                field -> {
                    Map<ContainerType, Object> screenFactories =
                            (Map<ContainerType, Object>) field.get(null);
                    screenFactories.put(containerType, screenFactory);
                });
    }

    @SuppressWarnings("unchecked")
    private static Object createScreenFactory(Class<?> factoryIface, ScreenFactory factory) {
        return Proxy.newProxyInstance(factoryIface.getClassLoader(), new Class[] {factoryIface},
                (proxy, method, args) -> {
                    if ("create".equals(method.getName())) {
                        return factory.create((Container) args[0], (PlayerInventory) args[1],
                                (Component) args[2]);
                    } else {
                        Constructor<Lookup> constructor =
                                Lookup.class.getDeclaredConstructor(Class.class);
                        constructor.setAccessible(true);
                        return constructor.newInstance(factoryIface)
                                .in(factoryIface)
                                .unreflectSpecial(method, factoryIface)
                                .bindTo(proxy)
                                .invokeWithArguments(args);
                    }
                });
    }
}
