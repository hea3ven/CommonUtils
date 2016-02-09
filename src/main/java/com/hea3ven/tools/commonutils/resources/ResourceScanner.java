package com.hea3ven.tools.commonutils.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;

public abstract class ResourceScanner {
	protected static final Logger logger = LogManager.getLogger("CommonUtils.ResourceScanner");

	protected static boolean isModLoaded(String name) {
		for (ModContainer mod : Loader.instance().getActiveModList()) {
			if (mod.getModId().toLowerCase().equals(name)) {
				return Loader.instance().getModState(mod) != LoaderState.ModState.DISABLED;
			}
		}
		return false;
	}

	public abstract void addModDirectory(Path dir);

	public abstract Iterable<ResourceLocation> scan(String modid, String type);

	public abstract InputStream getResource(ResourceLocation resLoc) throws IOException;

	protected static Set<ResourceLocation> getResourcesFromDir(Path dir, String modid, String name) {
		Path assetsDir = dir.resolve("assets");
		if (!Files.exists(assetsDir))
			return Sets.newHashSet();

		try (DirectoryStream<Path> modDirs = Files.newDirectoryStream(assetsDir)) {
			Set<ResourceLocation> resources = new HashSet<>();
			for (Path modDir : modDirs) {
				String modName = modDir.getFileName().toString();
				if (!modName.equals(modid))
					continue;
				Path targetDir = modDir.resolve(name);
				if (!Files.exists(targetDir))
					continue;

				try (DirectoryStream<Path> modResDirs = Files.newDirectoryStream(targetDir)) {
					for (Path modResDir : modResDirs) {
						if (!Files.isDirectory(modResDir))
							continue;
						if (!isModLoaded(modResDir.getFileName().toString()))
							continue;
						try (DirectoryStream<Path> entries = Files.newDirectoryStream(modResDir)) {
							for (Path entry : entries) {
								if (!entry.getFileName().toString().endsWith(".json"))
									continue;
								resources.add(new ResourceLocation(modName,
										modDir.relativize(entry).toString().replace('\\', '/')));
							}
						}
					}
				}
			}
			return resources;
		} catch (IOException e) {
			return Sets.newHashSet();
		}
	}

	protected static Set<ResourceLocation> getResourcesFromZip(ZipFile zip, String modid, String name) {
		Set<ResourceLocation> resources = new HashSet<>();
		for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory())
				continue;
			Path entryPath = Paths.get(entry.getName());
			if (entryPath.getNameCount() < 5)
				continue;
			if (!entryPath.getName(0).getFileName().toString().equals("assets"))
				continue;
			if (!entryPath.getName(1).getFileName().toString().equals(modid))
				continue;
			if (!entryPath.getName(2).getFileName().toString().equals(name))
				continue;
			if (!isModLoaded(entryPath.getName(3).getFileName().toString()))
				continue;
			if (!entryPath.getFileName().toString().endsWith(".json"))
				continue;

			resources.add(new ResourceLocation(entryPath.getName(1).getFileName().toString(),
					entryPath.subpath(2, entryPath.getNameCount()).toString().replace('\\', '/')));
		}
		return resources;
	}
}
