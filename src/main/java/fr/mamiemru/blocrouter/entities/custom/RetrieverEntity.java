package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.blocks.custom.Retriever;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.RetrieverMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RetrieverEntity extends BaseEntityEnergy {

    public static final int SLOT_OUTPUT_SLOT = 0;
    public static final int SLOT_UPGRADE = 1;

    public static final int NUMBER_OF_SLOTS = 2;

    protected final ContainerData data;

    private int processTick = 0;
    private int processMaxTick = 96;

    private int numberOfItemsToTransferPerOperation = 1;

    public static final int NUMBER_OF_VARS = 3;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == SLOT_UPGRADE) {
                return stack.getItem() instanceof ItemProcessingUpgrade;
            } else if (slot == SLOT_OUTPUT_SLOT) {
                return true;
            }
            return super.isItemValid(slot, stack);
        }
    };

    private static boolean canExtract(int slot) {
        return slot == SLOT_OUTPUT_SLOT;
    }

    private static boolean canInsert(int slot, @NotNull ItemStack stack) {
        return false;
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of( Direction.UP,    LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, (index, stack) -> index == SLOT_OUTPUT_SLOT)),
                    Direction.DOWN,  LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RetrieverEntity::canExtract, RetrieverEntity::canInsert)),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RetrieverEntity::canExtract, RetrieverEntity::canInsert)),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RetrieverEntity::canExtract, RetrieverEntity::canInsert)),
                    Direction.EAST,  LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RetrieverEntity::canExtract, RetrieverEntity::canInsert)),
                    Direction.WEST,  LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RetrieverEntity::canExtract, RetrieverEntity::canInsert))
            );

    public RetrieverEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.RETRIEVER_ENTITY.get(), pos, state, 32000, 1024, 32);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RetrieverEntity.this.processTick;
                    case 1 -> RetrieverEntity.this.processMaxTick;
                    case 2 -> RetrieverEntity.this.numberOfItemsToTransferPerOperation;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RetrieverEntity.this.processTick = value;
                    case 1 -> RetrieverEntity.this.processMaxTick = value;
                    case 2 -> RetrieverEntity.this.numberOfItemsToTransferPerOperation = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Retriever");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RetrieverMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return super.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(Retriever.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                };
            }
        } else if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("RetrieverEntity.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("RetrieverEntity.processTick", processTick);
        nbt.putInt("RetrieverEntity.processMaxTick", processMaxTick);
        nbt.putInt("RetrieverEntity.numberOfItemsToTransferPerOperation", numberOfItemsToTransferPerOperation);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("RetrieverEntity.inventory"));
        processTick = nbt.getInt("RetrieverEntity.processTick");
        processMaxTick = nbt.getInt("RetrieverEntity.processMaxTick");
        numberOfItemsToTransferPerOperation = nbt.getInt("RetrieverEntity.numberOfItemsToTransferPerOperation");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private static void forEveryMachines(@NotNull RetrieverEntity pEntity, BlockEntity block_entity_reference) {

        Level level = pEntity.getLevel();
        BlockEntity block_entity_selected  = block_entity_reference;
        IItemHandler targetItemHandler = pEntity.getItemHandler(level, pEntity.getBlockPos(), Direction.UP);
        while (block_entity_selected != null && block_entity_reference.getClass().equals(block_entity_selected.getClass())) {

            BlockPos entity_pos = block_entity_selected.getBlockPos();
            IItemHandler sourceItemHandler = pEntity.getItemHandler(level, entity_pos, Direction.DOWN);

            if (sourceItemHandler != null) {
                processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, SLOT_OUTPUT_SLOT, SLOT_OUTPUT_SLOT, pEntity.numberOfItemsToTransferPerOperation);
            }

            block_entity_selected = level.getBlockEntity(block_entity_selected.getBlockPos().above());
        }
    }

    private int processMaxTickWithUpgrade() {
        ItemStack upgrade = itemStackHandler.getStackInSlot(SLOT_UPGRADE);
        if (upgrade != null && !upgrade.isEmpty() && upgrade.getItem() instanceof ItemProcessingUpgrade) {
            return processMaxTick/(((ItemProcessingUpgrade)upgrade.getItem()).getEfficiency());
        }
        return processMaxTick;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RetrieverEntity pEntity) {
        if (level.isClientSide() || !state.getValue(Retriever.ENABLED)) {
            return;
        }

        if (pEntity.hasEnoughEnergy()) {
            pEntity.useEnergy();
            if (pEntity.processMaxTickWithUpgrade() <= pEntity.processTick) {
                BlockEntity block_entity_reference = level.getBlockEntity(pos.above());
                forEveryMachines(pEntity, block_entity_reference);
                pEntity.processTick = 0;
            } else {
                ++pEntity.processTick;
            }
        }
    }

    @Override
    public int getNumberOfSlots() {
        return RetrieverEntity.NUMBER_OF_SLOTS;
    }
}
