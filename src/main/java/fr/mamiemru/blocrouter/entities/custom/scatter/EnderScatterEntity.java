package fr.mamiemru.blocrouter.entities.custom.scatter;

import fr.mamiemru.blocrouter.blocks.custom.scatter.Scatter;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.WrappedHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderScatterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EnderScatterEntity extends BaseEntityEnergy {

    public static final int SLOT_INPUT_SLOT = 0;
    public static final int SLOT_INPUT_TRANSFER_MIN = 1;
    public static final int SLOT_INPUT_TRANSFER_MAX = 18;
    public static final int SLOT_UPGRADE = 19;

    public static final int NUMBER_OF_SLOTS = 20;

    protected final ContainerData data;

    private int processTick = 0;
    private int processMaxTick = 96;

    public static final int NUMBER_OF_VARS = 3;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (stack.getItem() instanceof ItemProcessingUpgrade) {
                return slot == SLOT_UPGRADE;
            } else if (stack.getItem() instanceof ItemTeleportationSlot) {
                return SLOT_INPUT_TRANSFER_MIN <= slot && slot <= SLOT_INPUT_TRANSFER_MAX;
            }
            return super.isItemValid(slot, stack);
        }
    };

    private static boolean canExtract(int i) {
        return false;
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack)))
            );

    public EnderScatterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_SCATTER_ENTITY.get(), pos, state, 32000, 1024, 32);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> EnderScatterEntity.this.processTick;
                    case 1 -> EnderScatterEntity.this.processMaxTick;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> EnderScatterEntity.this.processTick = value;
                    case 1 -> EnderScatterEntity.this.processMaxTick = value;
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
        return Component.literal("Ender Scatter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderScatterMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return super.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(Scatter.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
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
        nbt.put("EnderScatterEntity.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("EnderScatterEntity.processTick", processTick);
        nbt.putInt("EnderScatterEntity.processMaxTick", processMaxTick);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("EnderScatterEntity.inventory"));
        processTick = nbt.getInt("EnderScatterEntity.processTick");
        processMaxTick = nbt.getInt("EnderScatterEntity.processMaxTick");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private int getNumberOfItemsToTransferPerOperation() {
        /*
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_QUANTITY);
        if (is.getItem() instanceof ItemQuantityUpgrade) {
            return ItemQuantityUpgrade.getQuantity(is);
        }*/
        return 1;
    }

    private int processMaxTickWithUpgrade() {
        ItemStack upgrade = itemStackHandler.getStackInSlot(SLOT_UPGRADE);
        if (upgrade != null && !upgrade.isEmpty() && upgrade.getItem() instanceof ItemProcessingUpgrade) {
            return processMaxTick/(((ItemProcessingUpgrade)upgrade.getItem()).getEfficiency());
        }
        return processMaxTick;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderScatterEntity pEntity) {
        if (level.isClientSide() || !pEntity.isEnabled()) {
            return;
        }

        if (pEntity.hasEnoughEnergy()) {
            if (pEntity.processMaxTickWithUpgrade() <= pEntity.processTick) {
                for (int transferSlotId = SLOT_INPUT_TRANSFER_MIN; transferSlotId <= SLOT_INPUT_TRANSFER_MAX; ++transferSlotId) {
                    pEntity.useEnergy();
                    ItemStack teleportationSlot = pEntity.itemStackHandler.getStackInSlot(transferSlotId);
                    ItemStack itemsToTransfer   = pEntity.itemStackHandler.extractItem(SLOT_INPUT_SLOT, pEntity.getNumberOfItemsToTransferPerOperation(), true);
                    if (teleportationSlot != null && !teleportationSlot.isEmpty() && teleportationSlot.getItem() instanceof ItemTeleportationSlot) {
                        BlockPos blockPos = ItemTeleportationSlot.getCoordinates(teleportationSlot);
                        IItemHandler targetItemHandlerOutput = pEntity.getItemHandler(level, blockPos, Direction.UP);
                        if (targetItemHandlerOutput != null) {
                            for (int idSlot = 0; idSlot < targetItemHandlerOutput.getSlots(); ++idSlot) {
                                if (processExtractionFromSelfToDistant(pEntity.itemStackHandler, targetItemHandlerOutput, itemsToTransfer, SLOT_INPUT_SLOT, idSlot)) {
                                    break;
                                }
                            }
                        }
                    }
                }
                pEntity.processTick = 0;
            } else {
                ++pEntity.processTick;
            }
        }
    }

    @Override
    public int getNumberOfSlots() {
        return EnderScatterEntity.NUMBER_OF_SLOTS;
    }
}
