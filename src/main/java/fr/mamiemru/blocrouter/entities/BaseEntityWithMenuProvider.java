package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.entities.custom.routers.RouterEntity;
import fr.mamiemru.blocrouter.util.patterns.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Nullable
    protected static IEnergyStorage getEnergyHandler(Level level, BlockPos pos, Direction side) {
        BlockEntity te = level.getBlockEntity(pos);
        return (te != null)? te.getCapability(ForgeCapabilities.ENERGY, side).orElse(null) : null;
    }

    protected static boolean itemStackAreEqual(ItemStack itemsToTransfer, ItemStack is) {
        return itemsToTransfer.getCount() == is.getCount() && itemsToTransfer.getItem().equals(is.getItem());
    }

    protected static boolean itemStackIsGreaterEqual(ItemStack itemsToTransfer, ItemStack is) {
        return itemsToTransfer.getCount() <= is.getCount() && itemsToTransfer.getItem().equals(is.getItem());
    }

    protected static int canTransferFromSelfToDistant(ItemStack simulatedExtract, IItemHandler targetInventory) {
        for (int targetSlot = 0; targetSlot < targetInventory.getSlots(); ++targetSlot) {
            if (canTransferFromSelfToDistant(simulatedExtract, targetInventory, targetSlot)) {
                return targetSlot;
            }
        }
        return -1;
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

    protected int searchForItemStackInInventory(IItemHandler inventory, ItemStack toSearch) {
        return searchForItemStackInInventory(inventory, toSearch, 0, inventory.getSlots());
    }

    protected int searchForItemStackInInventory(IItemHandler inventory, ItemStack toSearch, int start_slot, int stop_slot) {

        for (int i = start_slot; i <= stop_slot; ++i) {
            ItemStack toTest = inventory.getStackInSlot(i);
            if (toTest != null && toSearch.getItem() == toTest.getItem() && toSearch.getCount() <= toTest.getCount()) {
                return i;
            }
        }

        return -1;
    }

    protected static List<ItemStack> extractIngredientList(@NotNull BaseEntityWithMenuProvider pEntity, @NotNull ItemStackHandler itemStackHandler, @NotNull List<ItemStack> ingredients, int start_slot, int stop_slot, boolean simulate) {
        List<ItemStack> items = new ArrayList<>(ingredients.size());
        for (ItemStack is : ingredients) {
            if (!is.isEmpty()) {
                boolean found = false;
                for (int i = start_slot; i <= stop_slot; ++i) {
                    ItemStack test = itemStackHandler.extractItem(i, is.getCount(), true);
                    if (test != null && itemStackAreEqual(is, test)) {
                        items.add(itemStackHandler.extractItem(i, is.getCount(), simulate));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return List.of();
                }
            }
        }
        return items;
    }

    protected static void processExtractionFromSelfToDistantWithPattern(@NotNull BaseEntityWithMenuProvider pEntity, @NotNull ItemStackHandler itemStackHandler, @NotNull NormalRoutingPattern pattern, @NotNull BlockPos entity_pos, int start_slot, int stop_slot) {

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

    protected static boolean canCraft(ItemStackHandler itemStackHandler, ItemsPattern pattern, int start_slot, int stop_slot) {
        if (pattern != null) {
            return canCraft(itemStackHandler, pattern.getRows(), start_slot, stop_slot);
        }
        return false;
    }

    protected static boolean canCraft(ItemStackHandler itemStackHandler, RowsPattern pattern, int start_slot, int stop_slot) {
        if (pattern != null) {
            return canCraft(
                    itemStackHandler,
                    pattern.getRows().stream().filter(patternRow -> patternRow != null).map(patternRow -> patternRow.is).collect(Collectors.toList()),
                    start_slot, stop_slot
            );
        }
        return false;
    }

    protected static boolean canCraft(ItemStackHandler itemStackHandler, RowsCoordinatesPattern pattern, int start_slot, int stop_slot) {
        if (pattern != null) {
            return canCraft(
                    itemStackHandler,
                    pattern.getRows().stream().filter(patternRow -> patternRow != null).map(patternRow -> patternRow.is).collect(Collectors.toList()),
                    start_slot, stop_slot
            );
        }
        return false;
    }

    protected static boolean canCraft(ItemStackHandler itemStackHandler, List<ItemStack> ingredients, int start_slot, int stop_slot) {
        for (ItemStack is : ingredients) {
            if (!is.isEmpty()) {
                boolean find = false;
                for (int i = start_slot; i <= stop_slot; ++i) {
                    ItemStack test = itemStackHandler.extractItem(i, is.getCount(), true);
                    if (test != null && test.getCount() == is.getCount() && test.getItem().equals(is.getItem())) {
                        find = true;
                    }
                }
                if (!find) {
                    return false;
                }
            }
        }
        return true;
    };

    protected static void dropItemOnGround(Level pLevel, ItemStack pStack, BlockPos target, double extraHeight) {
        while(!pStack.isEmpty()) {
            double d0 = EntityType.ITEM.getWidth();
            double d2 = d0 / 2.0D + 0.5D;
            double d3 = Math.floor(target.getX()) + d2;
            double d4 = Math.floor(target.getY()) + extraHeight;
            double d5 = Math.floor(target.getZ()) + d2;
            ItemEntity itementity = new ItemEntity(pLevel, d3, d4, d5, pStack.split(pLevel.random.nextInt(21) + 10));
            itementity.setDeltaMovement(0, 0, 0);
            pLevel.addFreshEntity(itementity);
        }
    }

    protected static List<ItemEntity> grabItemOnGround(Level pLevel, BlockPos coordinates, List<ItemStack> filters) {
        AABB aabb = new AABB(coordinates);
        return pLevel.getEntitiesOfClass(ItemEntity.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);
    }

    protected static void dropContentOnGround(BlockEntity entity, List<ItemStack> itemStackList, BlockPos target, double extraHeight) {
        for (ItemStack pStack : itemStackList) {
            dropItemOnGround(entity.getLevel(), pStack, target, extraHeight);
        }
    }

    public BlockState getBlockStateFromCoordinates(BlockPos blockPos) {
        return getLevel().getBlockState(blockPos);
    }
}
