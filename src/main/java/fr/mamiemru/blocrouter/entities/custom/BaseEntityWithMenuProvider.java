package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.util.Pattern;
import fr.mamiemru.blocrouter.util.PatternRow;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseEntityWithMenuProvider extends BlockEntity implements MenuProvider {

    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public BaseEntityWithMenuProvider(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public abstract int getNumberOfSlots();

    @Nullable
    protected static IItemHandler getItemHandler(Level level, BlockPos pos, Direction side) {
        BlockEntity te = level.getBlockEntity(pos);
        return (te != null)? te.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null) : null;
    }

    protected static boolean itemStackAreEqual(ItemStack itemsToTransfer, ItemStack is) {
        return itemsToTransfer.getCount() == is.getCount() && itemsToTransfer.getItem().equals(is.getItem());
    }

    protected static boolean canTransferFromSelfToDistant(ItemStack simulatedExtract, IItemHandler targetInventory, int targetSlot) {
        if (targetSlot < targetInventory.getSlots()) {
            ItemStack targetedSimulated = targetInventory.insertItem(targetSlot, simulatedExtract, true);
            return simulatedExtract != null && !simulatedExtract.isEmpty() &&
                    targetInventory.isItemValid(targetSlot, simulatedExtract) && targetedSimulated.isEmpty();
        }
        return false;
    }
    
    protected static boolean canTransferFromDistantToSelf(ItemStack simulatedExtract, IItemHandler targetInventory, int targetSlot) {
        if (targetSlot < targetInventory.getSlots()) {
            ItemStack targetItemStack = targetInventory.getStackInSlot(targetSlot);
            return simulatedExtract != null && !simulatedExtract.isEmpty() &&
                    (targetItemStack.isEmpty() || targetItemStack.getItem().equals(simulatedExtract.getItem())) &&
                    targetItemStack.getCount() < targetItemStack.getMaxStackSize();
        }
        return false;
    }

    protected static boolean processExtractionFromSelfToDistant(@NotNull IItemHandler sourceInventory, @NotNull IItemHandler targetInventory, @NotNull ItemStack itemsToTransfer, int sourceSlotId, int targetSlotId) {
        if (canTransferFromSelfToDistant(itemsToTransfer, targetInventory, targetSlotId)) {
            targetInventory.insertItem(targetSlotId, itemsToTransfer, false);
            sourceInventory.extractItem(sourceSlotId, itemsToTransfer.getCount(), false);
            return true;
        }
        return false;
    }
    
    protected static void processExtractionFromDistantToSelf(@NotNull IItemHandler sourceItemHandle, @NotNull IItemHandler targetItemHandle, int targetSlotMin, int targetSlotMax, int numberOfItemsToTransferPerOperation) {

        for (int slot = 0; slot < sourceItemHandle.getSlots(); ++slot) {
            for (int outputSlot = targetSlotMin; outputSlot <= targetSlotMax; ++outputSlot) {
                if (slot < sourceItemHandle.getSlots() && outputSlot < targetItemHandle.getSlots()) {
                    ItemStack simulatedExtract = sourceItemHandle.extractItem(slot, numberOfItemsToTransferPerOperation, true);
                    if (canTransferFromDistantToSelf(simulatedExtract, targetItemHandle, outputSlot)){
                        ItemStack remainder = targetItemHandle.insertItem(outputSlot, simulatedExtract, false);
                        if (remainder.isEmpty()) {
                            sourceItemHandle.extractItem(slot, simulatedExtract.getCount(), false);
                        }
                    }
                }
            }
        }
    }

    protected static void processExtractionFromSelfToDistantWithPattern(@NotNull BaseEntityWithMenuProvider pEntity, @NotNull ItemStackHandler itemStackHandler, @NotNull Pattern pattern, @NotNull BlockPos entity_pos, int start_slot, int stop_slot) {

        boolean canInsertAllIngredients = true;

        IItemHandler[] targetInventories = new IItemHandler[pattern.getRows().size()];
        ItemStack[] itemsToTransfers = new ItemStack[pattern.getRows().size()];
        int[] insertionSlot = new int[pattern.getRows().size()];
        int[] extractionSlot = new int[pattern.getRows().size()];

        Direction[] directions = { null, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN };
        for (int idRow = 0; idRow < pattern.getRows().size(); ++idRow) {
            PatternRow patternRow = pattern.getRows().get(idRow);

            if (!patternRow.is.isEmpty()) {
                for (int i = start_slot; i <= stop_slot; ++i) {

                    ItemStack itemsToTransfer = itemStackHandler.extractItem(i, patternRow.is.getCount(), true);
                    IItemHandler targetInventory = RouterEntity.getItemHandler(pEntity.getLevel(), entity_pos, directions[patternRow.axe]);
                    if (targetInventory != null && itemStackAreEqual(patternRow.is, itemsToTransfer)) {

                        if (patternRow.slot < targetInventory.getSlots() && canTransferFromSelfToDistant(itemsToTransfer, targetInventory, patternRow.slot)) {
                            targetInventories[idRow] = targetInventory;
                            itemsToTransfers[idRow] = itemsToTransfer;
                            insertionSlot[idRow] = patternRow.slot;
                            extractionSlot[idRow] = i;
                        } else {
                            canInsertAllIngredients = false;
                        }
                        break;
                    }
                }
            }
        }

        if (canInsertAllIngredients) {
            for (int idRow = 0; idRow < pattern.getRows().size(); ++idRow) {
                if (targetInventories[idRow] != null) {
                    targetInventories[idRow].insertItem(insertionSlot[idRow], itemsToTransfers[idRow], false);
                    itemStackHandler.extractItem(extractionSlot[idRow], itemsToTransfers[idRow].getCount(), false);
                }
            }
        }
    }

    protected static boolean canCraft(ItemStackHandler itemStackHandler, Pattern pattern, int start_slot, int stop_slot) {
        if (pattern != null) {
            for (PatternRow patternRow : pattern.getRows()) {
                if (!patternRow.is.isEmpty()) {
                    boolean find = false;
                    for (int i = start_slot; i <= stop_slot; ++i) {
                        ItemStack test = itemStackHandler.extractItem(i, patternRow.is.getCount(), true);
                        if (test != null && test.getCount() == patternRow.is.getCount() && test.getItem().equals(patternRow.is.getItem())) {
                            find = true;
                        }
                    }
                    if (!find) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    static void print(String s) {
        if (Minecraft.getInstance().player != null)
            Minecraft.getInstance().player.displayClientMessage(Component.literal(s), false);
    }
}
