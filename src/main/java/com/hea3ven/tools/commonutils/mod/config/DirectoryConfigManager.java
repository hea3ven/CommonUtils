package com.hea3ven.tools.commonutils.mod.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.IConfigElement;

public class DirectoryConfigManager extends ConfigManager {
	private final List<ConfigManager> subCfgMgrs;
	private ConfigManager delegateCfgMgr;

	public DirectoryConfigManager(String modId, String name, String desc, Path path,
			List<ConfigManager> subCfgMgrs) {
		super(modId, name, desc);
		this.subCfgMgrs = subCfgMgrs;

		int count = 0;
		for (ConfigManager subCfgMgr : subCfgMgrs) {
			if (subCfgMgr.getConfigElements().size() > 0) {
				count++;
				delegateCfgMgr = subCfgMgr;
			}
		}
		if (count > 1)
			delegateCfgMgr = null;
	}

	@Override
	public String getDesc() {
		if (delegateCfgMgr != null)
			return delegateCfgMgr.getDesc();
		return super.getDesc();
	}

	@Override
	public List<IConfigElement> getConfigElements() {
		if (delegateCfgMgr != null)
			return delegateCfgMgr.getConfigElements();

		List<IConfigElement> elems = new ArrayList<>();
		for (ConfigManager subCfgMgr : subCfgMgrs) {
			List<IConfigElement> subElems = subCfgMgr.getConfigElements();
			if (subElems.size() > 0)
				elems.add(new DummyCategoryElement("", "", subElems));
		}
		return elems;
	}

	@Override
	protected void reload(boolean initial) {
		for (ConfigManager subCfgMgr : subCfgMgrs) {
			subCfgMgr.reload(initial);
		}
	}
}
