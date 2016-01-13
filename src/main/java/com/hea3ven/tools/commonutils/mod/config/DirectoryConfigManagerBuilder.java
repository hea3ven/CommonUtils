package com.hea3ven.tools.commonutils.mod.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Throwables;

public class DirectoryConfigManagerBuilder implements ConfigManagerBuilder {
	private String name;
	private String desc;
	private String dirName;
	private List<ConfigManagerBuilder> configFiles = new ArrayList<>();

	public DirectoryConfigManagerBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public DirectoryConfigManagerBuilder setDesc(String desc) {
		this.desc = desc;
		return this;
	}
	public DirectoryConfigManagerBuilder setDirName(String dirName) {
		this.dirName = dirName;
		return this;
	}
	public DirectoryConfigManagerBuilder addFile(ConfigManagerBuilder cfgMgrBuilder) {
		configFiles.add(cfgMgrBuilder);
		return this;
	}

	@Override
	public ConfigManager build(String modId, Path path) {
		Path dirPath = path.resolve(dirName);
		if (!Files.exists(dirPath)) {
			try {
				Files.createDirectories(dirPath);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
		List<ConfigManager> cfgMgrs = new ArrayList<>();
		for (ConfigManagerBuilder cfgMgrBuilder : configFiles) {
			ConfigManager cfgMgr = cfgMgrBuilder.build(modId, dirPath);
			if (cfgMgr != null)
				cfgMgrs.add(cfgMgr);
		}
		return new DirectoryConfigManager(modId, name, desc, dirPath, cfgMgrs);
	}
}
