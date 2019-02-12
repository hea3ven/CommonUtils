package com.hea3ven.tools.commonutils.mod;

import net.fabricmc.loader.launch.common.FabricLauncherBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.base.Throwables;

import com.hea3ven.tools.commonutils.mod.info.BlockInfo;
import com.hea3ven.tools.commonutils.mod.info.ContainerInfo;
import com.hea3ven.tools.commonutils.mod.info.EnchantmentInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemGroupInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemInfo;
import com.hea3ven.tools.commonutils.mod.info.ScreenInfo;

public class ModComposite extends Mod {
    private Map<String, ModModule> children = new HashMap<>();

    public ModComposite(String modId) {
        super(modId);
    }

    public void addModule(String name, String modId, String clsName) {
        // TODO: Conditional loading
        //		if (!Loader.isModLoaded(modId))
        //			return;
        addModule(name, clsName);
    }

    public void addModule(String name, String clsName) {
        boolean singleton = false;
        if (clsName.endsWith(".INSTANCE")) {
            singleton = true;
            clsName = clsName.substring(0, clsName.length() - 9);
        }
        Class<? extends ModModule> cls;
        try {
            //			cls = Loader.instance().getModClassLoader().loadClass(clsName).asSubclass(ModModule.class);
            ClassLoader targetClassLoader = FabricLauncherBase.getLauncher().getTargetClassLoader();
            cls = targetClassLoader
                    .loadClass(clsName)
                    .asSubclass(ModModule.class);
        } catch (ClassNotFoundException e) {
            Throwables.propagate(e);
            return;
        }
        ModModule child;
        try {
            if (!singleton) {
                child = cls.getConstructor().newInstance();
            } else {
                child = (ModModule) cls.getField("INSTANCE").get(null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not build the module " + name, e);
        }
        children.put(name, child);
        child.setParent(this);
    }

    public ModModule getModule(String name) {
        return children.get(name);
    }

    public String getModuleName(ModModule module) {
        return children.entrySet()
                .stream()
                .filter(e -> e.getValue() == module)
                .findAny()
                .map(Entry::getKey)
                .orElse(null);
    }

    @Override
    public void onPreInit() {
        children.values().stream().forEach(Mod::onPreInit);
    }

    @Override
    public void onInit() {
        children.values().stream().forEach(Mod::onInit);
    }

    @Override
    public void onPostInit() {
        children.values().stream().forEach(Mod::onPostInit);
    }

    @Override
    public Map<String, BlockInfo> getBlocks() {
        return children.values()
                .stream()
                .flatMap(module -> module.getBlocks().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, ContainerInfo> getContainers() {
        return children.values()
                .stream()
                .flatMap(module -> module.getContainers().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, ScreenInfo> getScreens() {
        return children.values()
                .stream()
                .flatMap(module -> module.getScreens().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, ItemInfo> getItems() {
        return children.values()
                .stream()
                .flatMap(module -> module.getItems().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, EnchantmentInfo> getEnchantments() {
        return children.values()
                .stream()
                .flatMap(module -> module.getEnchantments().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, ItemGroupInfo> getCreativeTabs() {
        return children.values()
                .stream()
                .flatMap(module -> module.getCreativeTabs().entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
