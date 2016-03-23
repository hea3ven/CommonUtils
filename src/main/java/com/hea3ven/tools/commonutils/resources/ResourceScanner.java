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

public abstract class ResourceScanner {
	protected static final Logger logger = LogManager.getLogger("CommonUtils.ResourceScanner");

	public abstract void addModDirectory(Path dir);

	public abstract Iterable<ResourceLocation> scan(String type);

	public abstract InputStream getResource(ResourceLocation resLoc) throws IOException;

	public abstract Iterable<InputStream> getAllResources(ResourceLocation resLoc) throws IOException;

	protected static Set<ResourceLocation> getResourcesFromDir(Path dir, String name) {
		Path assetsDir = dir.resolve("assets");
		if (!Files.exists(assetsDir))
			return Sets.newHashSet();

		try (DirectoryStream<Path> modDirs = Files.newDirectoryStream(assetsDir)) {
			Set<ResourceLocation> resources = new HashSet<>();
			for (Path modDir : modDirs) {
				Path targetDir = modDir.resolve(name);
				if (!Files.exists(targetDir))
					continue;

				try (DirectoryStream<Path> modResDirs = Files.newDirectoryStream(targetDir)) {
					for (Path entry : modResDirs) {
						if (!entry.getFileName().toString().endsWith(".json"))
							continue;
						resources.add(new ResourceLocation(modDir.getFileName().toString(),
								modDir.relativize(entry).toString().replace('\\', '/')));
					}
				}
			}
			return resources;
		} catch (IOException e) {
			return Sets.newHashSet();
		}
	}

	protected static Set<ResourceLocation> getResourcesFromZip(ZipFile zip, String name) {
		Set<ResourceLocation> resources = new HashSet<>();
		for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory())
				continue;
			Path entryPath = Paths.get(entry.getName());
			if (entryPath.getNameCount() < 4)
				continue;
			if (!entryPath.getName(0).getFileName().toString().equals("assets"))
				continue;
			if (!entryPath.getName(2).getFileName().toString().equals(name))
				continue;
			if (!entryPath.getFileName().toString().endsWith(".json"))
				continue;

			resources.add(new ResourceLocation(entryPath.getName(1).getFileName().toString(),
					entryPath.subpath(2, entryPath.getNameCount()).toString().replace('\\', '/')));
		}
		return resources;
	}
}
