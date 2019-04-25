package com.hea3ven.tools.commonutils.mod.info;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

public class BlockInfo {

    private final Identifier id;
    private final Block block;
    private final BlockItem item;
    private BlockEntityType<?> blockEntityType;

    public BlockInfo(Identifier id, Block block, BlockItem item) {
        this.id = id;
        this.block = block;
        this.item = item;
    }

    public Identifier getId() {
        return id;
    }

    public Block getBlock() {
        return block;
    }

    public BlockItem getItem() {
        return item;
    }

    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityType<T> getBlockEntityType() {
        return (BlockEntityType<T>) blockEntityType;
    }

    public void setBlockEntityType(BlockEntityType blockEntityType) {
        this.blockEntityType = blockEntityType;
    }
}
