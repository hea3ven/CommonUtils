package com.hea3ven.tools.commonutils.mod.fabric;

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

import com.hea3ven.tools.commonutils.mod.Mod;
import com.hea3ven.tools.commonutils.mod.info.ScreenInfo;

public class FabricClientModHandler {

    static public void onInitializeClient(Mod mod) {
        mod.onPreInit();
        for (ScreenInfo screenInfo : mod.getScreens().values()) {
            ScreenProviderRegistry.INSTANCE.registerFactory(screenInfo.getId(),
                    screenInfo.getFactory());
        }
        mod.onInit();
        mod.onPostInit();
    }
}
