package com.hea3ven.tools.commonutils.mod;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.hea3ven.tools.commonutils.inventory.GenericGuiHandler;
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler;

public class ProxyModBase {

	private ModInitializerCommon modInitializer;

	private String modId;

	List<InfoBlock> blocks = Lists.newArrayList();
	List<InfoTileEntity> tiles = Lists.newArrayList();
	List<InfoItem> items = Lists.newArrayList();

	private GenericGuiHandler guiHandler = new GenericGuiHandler();
	private IGuiHandler overrideGuiHandler;

	public ProxyModBase(String modId) {
		this.modId = modId;
		switch (FMLCommonHandler.instance().getSide()) {
			case CLIENT:
				modInitializer = new ModInitializerClient();
				break;
			case SERVER:
				modInitializer = new ModInitializerServer();
				break;
		}
	}

	public void onPreInitEvent() {
		modInitializer.onPreInitEvent(this);
	}

	public void onInitEvent() {
		modInitializer.onInitEvent(this);
	}

	public void onPostInitEvent() {
		modInitializer.onPostInitEvent(this);
	}

	protected void addBlock(Block block) {
		addBlock(block, block.getUnlocalizedName());
	}

	protected void addBlock(Block block, String name) {
		addBlock(block, name, ItemBlock.class);
	}

	protected void addBlock(Block block, String name, Class<? extends ItemBlock> itemCls) {
		addBlock(block, name, itemCls, null);
	}

	protected void addBlock(Block block, String name, Class<? extends ItemBlock> itemCls,
			Object... itemArgs) {
		blocks.add(new InfoBlock(block, modId, name, itemCls, itemArgs));
	}

	protected void addBlockVariant(Block block, String name, Class<? extends ItemBlock> itemCls,
			Object[] itemArgs, IProperty variantProp, String variantSuffix,
			Map<Object, Integer> variantMetas) {
		blocks.add(new InfoBlockVariant(block, modId, name, itemCls, itemArgs, variantProp, variantSuffix,
				variantMetas));
	}

	protected void addTileEntity(Class<? extends TileEntity> tileCls, String name) {
		tiles.add(new InfoTileEntity(tileCls, name));
	}

	protected void addItem(Item item, String name) {
		items.add(new InfoItem(item, modId, name));
	}

	protected void addItem(Item item, String name, String[] variants) {
		items.add(new InfoItem(item, modId, name, variants));
	}
	protected void addGui(int id, ISimpleGuiHandler handler) {
		guiHandler.addGui(id, handler);
	}

	public void registerEnchantments() {
	}

	public void registerRecipes() {
	}

	String getModId() {
		return modId;
	}

	public void setGuiHandler(IGuiHandler handler) {
		overrideGuiHandler = handler;
	}

	public IGuiHandler getGuiHandler() {
		return overrideGuiHandler != null ? overrideGuiHandler : guiHandler;
	}
}
