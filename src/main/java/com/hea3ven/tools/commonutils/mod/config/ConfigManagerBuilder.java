package com.hea3ven.tools.commonutils.mod.config;

import javax.annotation.Nullable;
import java.nio.file.Path;

public interface ConfigManagerBuilder {
	@Nullable
	ConfigManager build(String modId, Path path);
}
