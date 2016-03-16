package com.hea3ven.tools.commonutils.mod;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.hea3ven.tools.commonutils.client.ModelBakerBase;
import com.hea3ven.tools.commonutils.client.settings.KeyBindingManager;
import com.hea3ven.tools.commonutils.inventory.GenericGuiHandler;
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler;
import com.hea3ven.tools.commonutils.mod.config.ConfigManager;
import com.hea3ven.tools.commonutils.mod.config.ConfigManagerBuilder;

public class ProxyModBase {

	private ModInitializerCommon modInitializer;

	protected String modId;

	List<InfoBlock> blocks = Lists.newArrayList();
	List<InfoTileEntity> tiles = Lists.newArrayList();
	List<InfoItem> items = Lists.newArrayList();
	List<IRecipe> recipes = Lists.newArrayList();
	Map<String, CreativeTabs> creativeTabs = Maps.newHashMap();

	ConfigManager cfgMgr;

	private GenericGuiHandler guiHandler = new GenericGuiHandler();
	private IGuiHandler overrideGuiHandler;

	ConfigManagerBuilder cfgMgrBuilder;
	private SimpleNetworkWrapper netChannel;
	KeyBindingManager keyBindingManager;
	private ConfigManagerBuilder configManagerBuilder;

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

	String getModId() {
		return modId;
	}

	public void onPreInitEvent(FMLPreInitializationEvent event) {
		modInitializer.onPreInitEvent(this, event);
	}

	public void onInitEvent(FMLInitializationEvent event) {
		modInitializer.onInitEvent(this);
	}

	public void onPostInitEvent(FMLPostInitializationEvent event) {
		modInitializer.onPostInitEvent(this);
	}

	protected void registerConfig() {

	}

	public void addConfigManager(ConfigManagerBuilder cfgMgrBuilder) {
		this.cfgMgrBuilder = cfgMgrBuilder;
	}

	public ConfigManagerBuilder getConfigManagerBuilder() {
		return configManagerBuilder;
	}

	ConfigManager getConfigManager() {
		return cfgMgr;
	}

	protected void registerBlocks() {
	}

	public Block addBlock(Block block) {
		addBlock(block, block.getUnlocalizedName());
		return block;
	}

	public Block addBlock(Block block, String name) {
		addBlock(block, name, ItemBlock.class);
		return block;
	}

	public Block addBlock(Block block, String name, Class<? extends ItemBlock> itemCls, Object... itemArgs) {
		blocks.add(new InfoBlock(block, modId, name, itemCls, itemArgs));
		return block;
	}

	public Block addBlockVariant(Block block, String name, Class<? extends ItemBlock> itemCls,
			Object[] itemArgs, IProperty variantProp, String variantSuffix,
			Map<Object, Integer> variantMetas) {
		blocks.add(new InfoBlockVariant(block, modId, name, itemCls, itemArgs, variantProp, variantSuffix,
				variantMetas));
		return block;
	}

	protected void registerTileEntities() {
	}

	public void addTileEntity(Class<? extends TileEntity> tileCls, String name) {
		tiles.add(new InfoTileEntity(tileCls, name));
	}

	protected void registerItems() {
	}

	public Item addItem(Item item, String name) {
		items.add(new InfoItem(item, modId, name));
		return item;
	}

	public Item addItem(Item item, String name, String[] variants) {
		items.add(new InfoItem(item, modId, name, variants));
		return item;
	}

	protected void registerCreativeTabs() {
	}

	public CreativeTabs addCreativeTab(String name, final Item icon) {
		CreativeTabs tab = new CreativeTabs(name) {
			@Override
			public Item getTabIconItem() {
				return icon;
			}
		};
		creativeTabs.put(name, tab);
		return tab;
	}

	public CreativeTabs addCreativeTab(String name, final ItemStack icon) {
		CreativeTabs tab = new CreativeTabs(name) {
			@Override
			public Item getTabIconItem() {
				return icon.getItem();
			}

			@Override
			public ItemStack getIconItemStack() {
				return icon;
			}
		};
		creativeTabs.put(name, tab);
		return tab;
	}

	public CreativeTabs getCreativeTab(String name) {
		return creativeTabs.get(name);
	}

	@SideOnly(Side.CLIENT)
	protected void registerModelBakers() {
	}

	protected void registerRecipes() {
	}

	protected void addRecipe(Block result, Object... recipe) {
		addRecipe(false, new ItemStack(result), recipe);
	}

	protected void addRecipe(Item result, Object... recipe) {
		addRecipe(false, new ItemStack(result), recipe);
	}

	protected void addRecipe(ItemStack result, Object... recipe) {
		addRecipe(false, result, recipe);
	}

	protected void addRecipe(boolean shapeless, ItemStack result, Object... recipe) {
		if (shapeless)
			recipes.add(new ShapelessOreRecipe(result, recipe));
		else
			recipes.add(new ShapedOreRecipe(result, recipe));
	}

	protected void addRecipe(IRecipe recipe) {
		recipes.add(recipe);
	}

	protected void addGui(int id, ISimpleGuiHandler handler) {
		guiHandler.addGui(id, handler);
	}

	@SideOnly(Side.CLIENT)
	protected void addModelBaker(ModelBakerBase modelBaker) {
		MinecraftForge.EVENT_BUS.register(modelBaker);
	}

	protected void registerEnchantments() {
	}

	protected void registerGuis() {
	}

	public void setGuiHandler(IGuiHandler handler) {
		overrideGuiHandler = handler;
	}

	public IGuiHandler getGuiHandler() {
		return overrideGuiHandler != null ? overrideGuiHandler : guiHandler;
	}

	protected void registerNetworkPackets() {
	}

	public <REQ extends IMessage, REPLY extends IMessage> void addNetworkPacket(
			Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType,
			int discriminator, Side side) {
		if (netChannel == null)
			netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(modId);
		netChannel.registerMessage(messageHandler, requestMessageType, discriminator, side);
	}

	public SimpleNetworkWrapper getNetChannel() {
		return netChannel;
	}

	protected void registerKeyBindings() {
	}

	public void addKeyBinding(String description, int keyCode, String category,
			Consumer<KeyInputEvent> callback) {
		if (keyBindingManager == null) {
			keyBindingManager = new KeyBindingManager();
		}
		keyBindingManager.addKeyBinding(description, keyCode, category, callback);
	}

	public void addItemKeyBinding(Item item, String description, int keyCode, String category,
			Consumer<KeyInputEvent> callback) {
		if (keyBindingManager == null) {
			keyBindingManager = new KeyBindingManager();
		}
		keyBindingManager.addItemKeyBinding(item, description, keyCode, category, callback);
	}

	public void addItemScrollWheelBinding(Item item, Function<MouseEvent, Boolean> callback) {
		if (keyBindingManager == null) {
			keyBindingManager = new KeyBindingManager();
		}
		keyBindingManager.addScrollWheelBinding(item, callback);
	}

	protected void registerCommands() {
	}

	public void addCommand(ICommand cmd) {
		ClientCommandHandler.instance.registerCommand(cmd);
	}
}
