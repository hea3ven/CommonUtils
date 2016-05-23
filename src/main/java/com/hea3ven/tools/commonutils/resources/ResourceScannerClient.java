package com.hea3ven.tools.commonutils.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceScannerClient extends ResourceScanner {
	@Override
	public InputStream getResource(ResourceLocation resLoc) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(resLoc).getInputStream();
	}

	@Override
	public Iterable<InputStream> getAllResources(ResourceLocation resLoc) throws IOException {
		return Minecraft.getMinecraft()
				.getResourceManager()
				.getAllResources(resLoc)
				.stream()
				.map(IResource::getInputStream)::iterator;
	}

	@Override
	public void addModDirectory(Path dir) {
		List<IResourcePack> resPacks =
				ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "field_110449_ao",
						"defaultResourcePacks");
		resPacks.add(new FolderResourcePack(dir.toFile()) {
			@Override
			public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer serializer,
					String section) throws IOException {
				return null;
			}
		});
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
			for (Path subPath : dirStream) {
				if (Files.isRegularFile(subPath)) {
					if (subPath.toString().endsWith(".zip")) {
						resPacks.add(new FileResourcePack(subPath.toFile()) {
							@Override
							public <T extends IMetadataSection> T getPackMetadata(
									MetadataSerializer serializer, String section) throws IOException {
								return null;
							}
						});
					}
				}
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}

	@Override
	public Iterable<ResourceLocation> scan(String name) {
		Set<ResourceLocation> res = new HashSet<>();
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		for (IResourceManager mgr : getDomainResourceManagers(resourceManager).values()) {
			for (IResourcePack resPack : getResourcePackages(mgr)) {
				res.addAll(getMaterials(resPack, name));
			}
		}
		return res;
	}

	private static List<IResourcePack> getResourcePackages(IResourceManager mgr) {
		return ReflectionHelper.getPrivateValue(FallbackResourceManager.class, (FallbackResourceManager) mgr,
				"field_110540_a", "resourcePacks");
	}

	private static Map<String, IResourceManager> getDomainResourceManagers(IResourceManager resourceManager) {
		return ReflectionHelper.getPrivateValue(SimpleReloadableResourceManager.class,
				(SimpleReloadableResourceManager) resourceManager, "field_110548_a",
				"domainResourceManagers");
	}

	private static Set<ResourceLocation> getMaterials(IResourcePack resPack, String name) {
		if (resPack instanceof FolderResourcePack) {
			File rootDir =
					ReflectionHelper.getPrivateValue(AbstractResourcePack.class, (FolderResourcePack) resPack,
							"field_110597_b", "resourcePackFile");
			return getResourcesFromDir(Paths.get(rootDir.toString()), name);
		} else if (resPack instanceof FileResourcePack) {
			ZipFile packFile = getZipFromResPack((FileResourcePack) resPack);
			return getResourcesFromZip(packFile, name);
		} else
			return Sets.newHashSet();
	}

	private static ZipFile getZipFromResPack(FileResourcePack resPack) {
		Method mthd = ReflectionHelper.findMethod(FileResourcePack.class, resPack,
				new String[] {"getResourcePackZipFile", "func_110599_c"});
		try {
			return (ZipFile) mthd.invoke(resPack);
		} catch (Exception e) {
			Throwables.propagate(e);
			return null;
		}
	}
}
