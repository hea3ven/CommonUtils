package com.hea3ven.tools.commonutils.mod;

import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder.CategoryConfigManagerBuilder;

public class ProxyModModule extends ProxyModBase {
	public ProxyModModule() {
		super(null);
		setGuiHandler(null);
	}

	void setParent(ProxyModComposite parent) {
		modId = parent.modId;
		guiHandler = parent.guiHandler;
	}

	public CategoryConfigManagerBuilder getConfig() {
		return null;
	}
}
