package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class PatternRowCoordinates extends PatternRow {

    private BlockPos blockPos;

    public PatternRowCoordinates(int slot, ItemStack is, int axe, BlockPos blockPos) {
        super(slot, is, axe);
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
