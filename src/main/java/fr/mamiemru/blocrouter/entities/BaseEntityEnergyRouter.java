package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.items.custom.*;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEntityEnergyRouter extends BaseEntityEnergy {

    protected List<Pattern> patterns = new ArrayList<>();

    protected abstract Pattern getCastedPattern(ItemStack is);
    protected abstract boolean isPatternRight(Item item);
    protected int getSlotOutputTeleportationCard() { return -1; }

    protected abstract int getSlotPatternSlot0();
    protected abstract int getNumberOfPatternSlots();

    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (isPatternRight(stack.getItem())) {
            return getSlotPatternSlot0() == slot;
        } else if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return slot == getSlotUpgrade();
        } else if (getSlotOutputSlot0() <= slot && slot <= getSlotOutputSlotN()) {
            return true;
        } else if (getSlotInputSlot0() <= slot && slot <= getSlotInputSlotN()) {
            return true;
        } else if (stack.getItem() instanceof ItemTeleportationSlot) {
            return slot == getSlotOutputTeleportationCard();
        }
        return false;
    }

    @Override
    protected void onSlotContentChanged(int slot) {
        if (getSlotPatternSlot0() <= slot && slot <= getSlotPatternSlot0() + getNumberOfSlots()) {
            loadPatterns();
        }
    }

    private void loadPatterns() {
        BaseEntityEnergyRouter.this.patterns.clear();
        for (int patternSlot = 0; patternSlot < getNumberOfPatternSlots(); ++patternSlot) {
            ItemStack is = itemStackHandler.getStackInSlot(getSlotPatternSlot0() + patternSlot);
            if (is != null && !is.isEmpty()) {
                BaseEntityEnergyRouter.this.patterns.add(patternSlot, getCastedPattern(is));
            } else {
                BaseEntityEnergyRouter.this.patterns.add(patternSlot, null);
            }
        }
    }

    public BaseEntityEnergyRouter(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state, 64000, 1024, 32);
    }
    protected void handleExtraction() {};
    protected void handleProcessing() {};

    protected abstract void handleExtraction(@NotNull Pattern pattern);
    protected abstract void handleProcessing(@NotNull Pattern pattern);

    private static void forPattern(@NotNull BaseEntityEnergyRouter pEntity, @NotNull Pattern pattern) {
        if (pattern == null) {
            return;
        }

        pEntity.useEnergy();
        pEntity.handleProcessing(pattern);
        pEntity.handleExtraction(pattern);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseEntityEnergyRouter pEntity) {

        if (level.isClientSide() || !pEntity.isEnabled() || !pEntity.hasEnoughEnergy()) {
            return;
        }

        if (pEntity.processTick < pEntity.processMaxTickWithUpgrade()) {
            ++pEntity.processTick;
            return;
        }

        if (pEntity.patterns.isEmpty()) {
            pEntity.loadPatterns();
        }

        for (Pattern pattern : pEntity.patterns) {
            forPattern(pEntity, pattern);
        }

        pEntity.processTick = 0;
    }
}