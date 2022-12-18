package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.EnderRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemEnderRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderRouterEntity extends BaseEntityEnergyRouter {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_OUTPUT_SLOT_0 = 9;
    public static final int SLOT_OUTPUT_SLOT_1 = 10;
    public static final int SLOT_OUTPUT_SLOT_2 = 11;
    public static final int SLOT_OUTPUT_SLOT_3 = 12;
    public static final int SLOT_OUTPUT_SLOT_4 = 13;
    public static final int SLOT_OUTPUT_SLOT_5 = 14;
    public static final int SLOT_UPGRADE = 15;
    public static final int SLOT_PATTERN_SLOT_0 = 16;
    public static final int SLOT_PATTERN_SLOT_1 = 17;
    public static final int SLOT_PATTERN_SLOT_2 = 18;
    public static final int SLOT_PATTERN_SLOT_3 = 19;
    public static final int NUMBER_OF_SLOTS = 20;

    public EnderRouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_ROUTER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Router");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderRouterMenu(id, inventory, this, this.data);
    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {

        int storageIndex = 0;
        boolean canInsertAllIngredients = true;
        EnderRoutingPattern enderRoutingPattern = (EnderRoutingPattern) pattern;

        int[] extractionSlot = new int[enderRoutingPattern.getRows().size()];
        ItemStack[] itemsToTransfers = new ItemStack[enderRoutingPattern.getRows().size()];
        IItemHandler[] targetInventories = new IItemHandler[enderRoutingPattern.getRows().size()];
        Direction[] directions = {null, Direction.UP, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.DOWN};

        if (canCraft(this.itemStackHandler, enderRoutingPattern, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8)) {

            for (PatternRowCoordinates patternRowCoordinates : enderRoutingPattern.getRows()) {
                boolean canProcessThisItem = false;
                for (int itemInputIndex = SLOT_INPUT_SLOT_0; itemInputIndex <= SLOT_INPUT_SLOT_8; ++itemInputIndex) {
                    ItemStack itemsToTransfer = itemStackHandler.extractItem(itemInputIndex, patternRowCoordinates.is.getCount(), true);
                    if (itemStackAreEqual(patternRowCoordinates.is, itemsToTransfer)) {
                        BlockPos targetEntityPos = patternRowCoordinates.getBlockPos();
                        IItemHandler targetInventory = getItemHandler(getLevel(), targetEntityPos, directions[patternRowCoordinates.axe]);
                        targetInventories[storageIndex] = targetInventory;
                        itemsToTransfers[storageIndex] = itemsToTransfer;
                        extractionSlot[storageIndex] = itemInputIndex;
                        ++storageIndex;
                        canProcessThisItem = true;
                        break;
                    }
                }

                if (!canProcessThisItem) {
                    canInsertAllIngredients = false;
                }
            }
        }

        if (canInsertAllIngredients) {
            for (int idRow = 0; idRow < enderRoutingPattern.getRows().size(); ++idRow) {
                if (targetInventories[idRow] != null) {
                    targetInventories[idRow].insertItem(0, itemsToTransfers[idRow], false);
                    itemStackHandler.extractItem(extractionSlot[idRow], itemsToTransfers[idRow].getCount(), false);
                }
            }
        }
    }
    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {
        EnderRoutingPattern enderRoutingPattern = (EnderRoutingPattern) pattern;
        for (BlockPos entity_pos : enderRoutingPattern.getOutputs()) {
            IItemHandler sourceInventory = getItemHandler(getLevel(),entity_pos, Direction.DOWN);

            if (sourceInventory != null) {
                for (int availableOutputSlots = 0; availableOutputSlots < sourceInventory.getSlots(); ++availableOutputSlots) {
                    int testSlotSize = sourceInventory.getSlotLimit(availableOutputSlots);
                    ItemStack simulatedExtract = sourceInventory.extractItem(availableOutputSlots, testSlotSize, true);
                    for (int availableInputSlot = getSlotOutputSlot0(); availableInputSlot <= getSlotOutputSlotN(); ++availableInputSlot) {
                        if (processExtractionFromSelfToDistant(sourceInventory, itemStackHandler, simulatedExtract, availableOutputSlots, availableInputSlot)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getNumberOfSlots() {
        return EnderRouterEntity.NUMBER_OF_SLOTS;
    }


    @Override
    protected Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemEnderRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
                ItemEnderRoutingPattern.decode(is.getTag()) : null;
    }

    @Override
    protected int getSlotPatternSlot0() {
        return SLOT_PATTERN_SLOT_0;
    }

    @Override
    protected int getNumberOfPatternSlots() {
        return 4;
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT_0;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT_8;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return SLOT_OUTPUT_SLOT_0;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return SLOT_OUTPUT_SLOT_5;
    }

    @Override
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemEnderRoutingPattern;
    }
}