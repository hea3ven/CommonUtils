package com.hea3ven.tools.commonutils.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;

public class ResourceScannerServer extends ResourceScanner {
	private List<Path> modDirectories = new ArrayList<>();
	private List<Path> modResourcePacks = new ArrayList<>();

	@Override
	public void addModDirectory(Path dir) {
		modDirectories.add(dir);
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
			for (Path subPath : dirStream) {
				if (Files.isRegularFile(subPath)) {
					if (subPath.toString().endsWith(".zip")) {
						modResourcePacks.add(subPath);
					}
				}
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public Iterable<ResourceLocation> scan(String name) {
		Set<ResourceLocation> resources = Sets.newHashSet();
		for (Path dir : modDirectories) {
			resources.addAll(getResourcesFromDir(dir, name));
		}
		for (Path resPack : modResourcePacks) {
			resources.addAll(scanZip(resPack, name));
		}
		for (final URL element : Launch.classLoader.getSources()) {
			if (!element.getProtocol().equals("file"))
				continue;
			try {
				Path elemPath = Paths.get(element.toURI());
				if (Files.isDirectory(elemPath)) {
					resources.addAll(getResourcesFromDir(elemPath, name));
				} else {
					resources.addAll(scanZip(elemPath, name));
				}
			} catch (URISyntaxException e) {
				logger.debug("could not scan an element of the classpath");
			}
		}
		return resources;
	}

	@Override
	public InputStream getResource(ResourceLocation resLoc) throws IOException {
		for (Path dir : modDirectories) {
			Path resPath = dir.resolve("assets")
					.resolve(resLoc.getResourceDomain())
					.resolve(resLoc.getResourcePath());
			if (Files.exists(resPath))
				return Files.newInputStream(resPath, StandardOpenOption.READ);
		}
		for (Path resPack : modResourcePacks) {
			try (ZipFile zip = new ZipFile(resPack.toFile())) {
				ZipEntry entry = zip.getEntry(
						String.format("assets/%s/%s", resLoc.getResourceDomain(), resLoc.getResourcePath()));
				if (entry != null) {
					ByteArrayOutputStream data = new ByteArrayOutputStream();
					try (InputStream entryStream = zip.getInputStream(entry)) {
						int length;
						byte[] buffer = new byte[2048];
						while ((length = entryStream.read(buffer)) > 0) {
							data.write(buffer, 0, length);
						}
						return new ByteArrayInputStream(data.toByteArray());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResourceScannerServer.class.getResourceAsStream(
				String.format("/assets/%s/%s", resLoc.getResourceDomain(), resLoc.getResourcePath()));
	}

	@Override
	public Iterable<InputStream> getAllResources(ResourceLocation resLoc) throws IOException {
		List<InputStream> resources = new ArrayList<>();
		for (Path dir : modDirectories) {
			Path resPath = dir.resolve("assets")
					.resolve(resLoc.getResourceDomain())
					.resolve(resLoc.getResourcePath());
			if (Files.exists(resPath))
				resources.add(Files.newInputStream(resPath, StandardOpenOption.READ));
		}
		for (Path resPack : modResourcePacks) {
			try (ZipFile zip = new ZipFile(resPack.toFile())) {
				ZipEntry entry = zip.getEntry(
						String.format("assets/%s/%s", resLoc.getResourceDomain(), resLoc.getResourcePath()));
				if (entry != null) {
					ByteArrayOutputStream data = new ByteArrayOutputStream();
					try (InputStream entryStream = zip.getInputStream(entry)) {
						int length;
						byte[] buffer = new byte[2048];
						while ((length = entryStream.read(buffer)) > 0) {
							data.write(buffer, 0, length);
						}
						resources.add(new ByteArrayInputStream(data.toByteArray()));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ClassLoader classLoader = ResourceScannerServer.class.getClassLoader();
		String path = String.format("assets/%s/%s", resLoc.getResourceDomain(), resLoc.getResourcePath());
		for (URL url : Collections.list(classLoader.getResources(path))) {
			resources.add(url.openStream());
		}
		return resources;
	}

	private Set<ResourceLocation> scanZip(Path zipPath, String name) {
		try (ZipFile zip = new ZipFile(zipPath.toFile())) {
			return getResourcesFromZip(zip, name);
		} catch (IOException e) {
			logger.error("Could not open the jar file", e);
			return Sets.newHashSet();
		}
	}
}
