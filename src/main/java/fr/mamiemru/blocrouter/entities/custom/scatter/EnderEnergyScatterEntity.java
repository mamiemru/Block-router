package fr.mamiemru.blocrouter.entities.custom.scatter;

import fr.mamiemru.blocrouter.blocks.BaseFacingBlock;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergySortMode;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.WrappedHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderEnergyScatterMenu;
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
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class EnderEnergyScatterEntity extends BaseEntityEnergySortMode {
    public static final int SLOT_INPUT_TRANSFER_MIN = 0;
    public static final int SLOT_INPUT_TRANSFER_MAX = 17;
    public static final int SLOT_UPGRADE = 18;
    public static final int NUMBER_OF_SLOTS = 19;
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
            if (slot == SLOT_UPGRADE) {
                return stack.getItem() instanceof ItemProcessingUpgrade;
            } else if (SLOT_INPUT_TRANSFER_MIN <= slot && slot <= SLOT_INPUT_TRANSFER_MAX) {
                return stack.getItem() instanceof ItemTeleportationSlot;
            }
            return super.isItemValid(slot, stack);
        }
    };

    private static boolean canExtract(int i) {
        return false;
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderEnergyScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderEnergyScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderEnergyScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderEnergyScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderEnergyScatterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack)))
            );

    public EnderEnergyScatterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_ENERGY_SCATTER_ENTITY.get(), pos, state, 2048000, 18432, 16);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> EnderEnergyScatterEntity.this.processTick;
                    case 1 -> EnderEnergyScatterEntity.this.processMaxTick;
                    case 2 -> EnderEnergyScatterEntity.this.slotPointer;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> EnderEnergyScatterEntity.this.processTick = value;
                    case 1 -> EnderEnergyScatterEntity.this.processMaxTick = value;
                    case 2 -> EnderEnergyScatterEntity.this.slotPointer = value;
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_VARS;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Energy Scatter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderEnergyScatterMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return super.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(BaseFacingBlock.FACING);

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
        nbt.put("EnderEnergyScatterEntity.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("EnderEnergyScatterEntity.processTick", processTick);
        nbt.putInt("EnderEnergyScatterEntity.processMaxTick", processMaxTick);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("EnderEnergyScatterEntity.inventory"));
        processTick = nbt.getInt("EnderEnergyScatterEntity.processTick");
        processMaxTick = nbt.getInt("EnderEnergyScatterEntity.processMaxTick");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private int processMaxTickWithUpgrade() {
        ItemStack upgrade = itemStackHandler.getStackInSlot(SLOT_UPGRADE);
        if (upgrade != null && !upgrade.isEmpty() && upgrade.getItem() instanceof ItemProcessingUpgrade) {
            return processMaxTick/(((ItemProcessingUpgrade)upgrade.getItem()).getEfficiency());
        }
        return processMaxTick;
    }

    private static void processPriority(EnderEnergyScatterEntity pEntity) {
        ItemStack is = pEntity.itemStackHandler.getStackInSlot(pEntity.slotPointer);
        pEntity.useEnergy();
        BlockPos blockPos = ItemTeleportationSlot.getCoordinates(is);
        IEnergyStorage energyStorage = pEntity.getEnergyHandler(pEntity.level, blockPos, null);
        if (energyStorage != null) {
            int simulated = energyStorage.receiveEnergy(pEntity.getEnergyMaxTransfer(), true);
            if (pEntity.getEnergyStorage().extractEnergy(simulated, true) == simulated) {
                pEntity.getEnergyStorage().extractEnergy(simulated, false);
                energyStorage.receiveEnergy(simulated, false);
            }

            int percentage = Math.floorDiv(Math.multiplyExact(energyStorage.getEnergyStored(), 100), energyStorage.getMaxEnergyStored());
            if (90 < percentage) {
                ++pEntity.slotPointer;
            }

        } else {
            ++pEntity.slotPointer;
        }
    }

    private static void processRoundRobin(EnderEnergyScatterEntity pEntity) {
        ItemStack is = pEntity.itemStackHandler.getStackInSlot(pEntity.slotPointer);
        pEntity.useEnergy();
        BlockPos blockPos = ItemTeleportationSlot.getCoordinates(is);
        IEnergyStorage energyStorage = pEntity.getEnergyHandler(pEntity.level, blockPos, null);
        if (energyStorage != null) {
            int simulated = energyStorage.receiveEnergy(pEntity.getEnergyMaxTransfer(), true);
            if (pEntity.getEnergyStorage().extractEnergy(simulated, true) == simulated) {
                pEntity.getEnergyStorage().extractEnergy(simulated, false);
                energyStorage.receiveEnergy(simulated, false);
            }
        }
        ++pEntity.slotPointer;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderEnergyScatterEntity pEntity) {
        if (level.isClientSide() || !pEntity.isEnabled() || !pEntity.hasEnoughEnergy()) {
            return;
        }

        if (pEntity.processTick < pEntity.processMaxTickWithUpgrade()) {
            ++pEntity.processTick;
            return;
        }

        ItemStack is = pEntity.itemStackHandler.getStackInSlot(pEntity.slotPointer);
        if (is != null && is.getItem() instanceof ItemTeleportationSlot && is.getCount() == 1 && is.hasTag()) {
            Comparable sortMethod = pEntity.getSortMethod();
            if (sortMethod == SortMode.ROUND_ROBIN) {
                processRoundRobin(pEntity);
            } else if (sortMethod == SortMode.PRIORITY) {
                processPriority(pEntity);
            }
        } else {
            ++pEntity.slotPointer;
        }

        if (pEntity.slotPointer == SLOT_INPUT_TRANSFER_MAX) {
            pEntity.slotPointer = SLOT_INPUT_TRANSFER_MIN;
        }

        pEntity.processTick = 0;
    }

    @Override
    public int getNumberOfSlots() {
        return EnderEnergyScatterEntity.NUMBER_OF_SLOTS;
    }

}
