package com.hea3ven.tools.commonutils.mod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.container.ContainerFactory;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.hea3ven.tools.commonutils.mod.info.BlockInfo;
import com.hea3ven.tools.commonutils.mod.info.ContainerInfo;
import com.hea3ven.tools.commonutils.mod.info.EnchantmentInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemGroupInfo;
import com.hea3ven.tools.commonutils.mod.info.ItemInfo;
import com.hea3ven.tools.commonutils.mod.info.ScreenInfo;

public class Mod {

    protected Logger logger;

    private String modId;

    private Map<String, BlockInfo> blocks = Maps.newHashMap();
    private Map<String, ContainerInfo> containers = Maps.newHashMap();
    private Map<String, ScreenInfo> screens = Maps.newHashMap();
    private Map<String, ItemInfo> items = Maps.newHashMap();
    private Map<String, EnchantmentInfo> enchantments = Maps.newHashMap();
    private Map<String, ItemGroupInfo> creativeTabs = Maps.newHashMap();

    //    ConfigManager cfgMgr;

    //	protected GenericGuiHandler guiHandler = new GenericGuiHandler();
    //	private IGuiHandler overrideGuiHandler;

    //	ConfigManagerBuilder cfgMgrBuilder;
    //	private SimpleNetworkWrapper netChannel;
    //	KeyBindingManager keyBindingManager;
    //	private ConfigManagerBuilder configManagerBuilder;

    private GameProfile fakePlayerProfile = null;

    public Mod(String modId) {
        this.modId = modId;
        this.logger = LogManager.getFormatterLogger(modId);
    }

    String getModId() {
        return modId;
    }

    protected Identifier id(String name) {
        return new Identifier(getModId(), name);
    }

    public void onPreInit() {
    }

    public void onInit() {
    }

    public void onPostInit() {
    }

    public Map<String, BlockInfo> getBlocks() {
        return blocks;
    }

    @Deprecated
    protected final void addBlock(String name, Block block) {
        addBlock(name, block, new BlockItem(block, new Item.Settings()));
    }

    protected final void addBlock(String name, Block block, ItemGroup group) {
        addBlock(name, block, new BlockItem(block, new Item.Settings().itemGroup(group)));
    }

    protected final void addBlock(String name, Block block, BlockItem item) {
        blocks.put(name, new BlockInfo(id(name), block, item));
    }

    protected final <T extends BlockEntity> void addBlock(String name, Block block, ItemGroup group,
            Function<Supplier<BlockEntityType<T>>, Supplier<T>> blockEntitySupplier) {
        this.addBlock(name, block, new BlockItem(block, new Item.Settings().itemGroup(group)),
                blockEntitySupplier);
    }

    protected final <T extends BlockEntity> void addBlock(String name, Block block, BlockItem item,
            Function<Supplier<BlockEntityType<T>>, Supplier<T>> blockEntitySupplier) {
        BlockInfo blockInfo = new BlockInfo(id(name), block, item);
        blockInfo.setBlockEntityType(new BlockEntityType<T>(blockEntitySupplier.apply(
                () -> (BlockEntityType<T>) blockInfo.getBlockEntityType()), null));
        blocks.put(name, blockInfo);
    }

    public Map<String, ContainerInfo> getContainers() {
        return containers;
    }

    protected final void addContainer(String name, ContainerFactory factory) {
        containers.put(name, new ContainerInfo(id(name), factory));
    }

    @Environment(EnvType.CLIENT)
    public Map<String, ScreenInfo> getScreens() {
        return screens;
    }

    @Environment(EnvType.CLIENT)
    protected final void addScreen(String name, ContainerScreenFactory factory) {
        screens.put(name, new ScreenInfo(id(name), factory));
    }

    public Map<String, ItemInfo> getItems() {
        return items;
    }

    public void addItem(String name, Item item) {
        items.put(name, new ItemInfo(id(name), item));
    }

    public Map<String, ItemGroupInfo> getCreativeTabs() {
        return creativeTabs;
    }

    public void addCreativeTab(String name, final ItemStack icon) {
        ItemGroup tab = new ItemGroup(-1, name) {
            @Override
            @Environment(EnvType.CLIENT)
            public ItemStack createIcon() {
                return icon;
            }
        };
        creativeTabs.put(name, new ItemGroupInfo(id(name), tab));
    }

    public Map<String, EnchantmentInfo> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(String name, Enchantment enchantment) {
        enchantments.put(name, new EnchantmentInfo(id(name), enchantment));
    }

    protected void registerNetworkPackets() {
    }

    //    public <REQ extends IMessage, REPLY extends IMessage> void addNetworkPacket(
    //            Class<? extends IMessageHandler<REQ, REPLY>> messageHandler,
    //            Class<REQ> requestMessageType, int discriminator, Side side) {
    //        if (netChannel == null)
    //            netChannel = NetworkRegistry.INSTANCE.newSimpleChannel(getModId());
    //        netChannel.registerMessage(messageHandler, requestMessageType, discriminator, side);
    //    }

    //    public SimpleNetworkWrapper getNetChannel() {
    //        return netChannel;
    //    }

    protected void registerKeyBindings() {
    }

    //    public void addKeyBinding(String description, int keyCode, String category,
    //            Consumer<KeyInputEvent> callback) {
    //        if (keyBindingManager == null) {
    //            keyBindingManager = new KeyBindingManager();
    //        }
    //        keyBindingManager.addKeyBinding(description, keyCode, category, callback);
    //    }

    //    public void addItemKeyBinding(Item item, String description, int keyCode, String category,
    //            Consumer<KeyInputEvent> callback) {
    //        if (keyBindingManager == null) {
    //            keyBindingManager = new KeyBindingManager();
    //        }
    //        keyBindingManager.addItemKeyBinding(item, description, keyCode, category, callback);
    //    }

    //    public void addItemScrollWheelBinding(Item item, Function<MouseEvent, Boolean> callback) {
    //        if (keyBindingManager == null) {
    //            keyBindingManager = new KeyBindingManager();
    //        }
    //        keyBindingManager.addScrollWheelBinding(item, callback);
    //    }

    protected void registerCommands() {
    }

    //    public void addCommand(ICommand cmd) {
    //        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
    //            ClientCommandHandler.instance.registerCommand(cmd);
    //    }

    public void setFakePlayerProfile(GameProfile fakePlayerProfile) {
        this.fakePlayerProfile = fakePlayerProfile;
    }

    public PlayerEntity getFakePlayer(World world) {
        if(fakePlayerProfile == null) {
            fakePlayerProfile = new GameProfile(null, "[" + getModId() + "]");
        }
        return new PlayerEntity(world, fakePlayerProfile) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
    }
}
