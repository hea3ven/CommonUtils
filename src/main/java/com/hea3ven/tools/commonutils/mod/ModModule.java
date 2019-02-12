package com.hea3ven.tools.commonutils.mod;

import org.apache.logging.log4j.LogManager;

public class ModModule extends Mod {

    private ModComposite parent;

    public ModModule() {
        super(null);
    }

    @Override
    String getModId() {
        return parent.getModId();
    }

    void setParent(ModComposite parent) {
        this.parent = parent;
        logger = LogManager.getFormatterLogger(getModId() + "." + parent.getModuleName(this));
    }
}
