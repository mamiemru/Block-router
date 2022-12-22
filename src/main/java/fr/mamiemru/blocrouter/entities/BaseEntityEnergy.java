package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.blocks.BaseFacingBlock;
import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.EnergySyncS2CPacket;
import fr.mamiemru.blocrouter.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class BaseEntityEnergy extends BaseEntityWithMenuProvider {

    protected final ContainerData data;
    protected ModEnergyStorage ENERGY_STORAGE;
    protected int energyProcess;
    protected int energyMaxTransfer;
    protected int processTick = 0;
    protected int processMaxTick = 96;
    protected LazyOptional<EnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected abstract int getSlotUpgrade();
    protected abstract void handleExtraction();
    protected abstract void handleProcessing();
    protected abstract int getSlotInputSlot0();
    protected abstract int getSlotInputSlotN();
    protected abstract int getSlotOutputSlot0();
    protected abstract int getSlotOutputSlotN();
    protected abstract void onSlotContentChanged(int slot);

    protected final ItemStackHandler itemStackHandler = new ItemStackHandler(getNumberOfSlots()) {
        @Override
        protected void onContentsChanged(int slot) {
            onSlotContentChanged(slot);
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) { return checkIsItemValid(slot, stack); }
    };

    protected boolean canExtractSide(int i) {
        return getSlotOutputSlot0() <= i && i <= getSlotOutputSlotN();
    }
    protected boolean canInsertSide(int slot, @NotNull ItemStack stack) {return getSlotInputSlot0() <= slot && slot <= getSlotInputSlotN();}

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of( Direction.UP,   LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, (index, stack) -> getSlotOutputSlot0() <= index && index <= getSlotOutputSlotN())),
                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergy.this::canExtractSide, BaseEntityEnergy.this::canInsertSide)),
                    Direction.NORTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergy.this::canExtractSide, BaseEntityEnergy.this::canInsertSide)),
                    Direction.SOUTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergy.this::canExtractSide, BaseEntityEnergy.this::canInsertSide)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergy.this::canExtractSide, BaseEntityEnergy.this::canInsertSide)),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergy.this::canExtractSide, BaseEntityEnergy.this::canInsertSide))
            );

    protected int containerDataGetter(int index) {
        return switch (index) {
            case 0 -> BaseEntityEnergy.this.processTick;
            case 1 -> BaseEntityEnergy.this.processMaxTick;
            default -> 0;
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 0 -> BaseEntityEnergy.this.processTick = value;
            case 1 -> BaseEntityEnergy.this.processMaxTick = value;
        }
    }
    protected int containerDataSize() {
        return 2;
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

    public BaseEntityEnergy(BlockEntityType<?> entityType, BlockPos pos, BlockState state, int energyCapacity, int energyMaxTransfer, int energyProcess) {
        super(entityType, pos, state);
        this.energyProcess = energyProcess;
        this.energyMaxTransfer = energyMaxTransfer;

        ENERGY_STORAGE = new ModEnergyStorage(energyCapacity, energyMaxTransfer) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                ModNetworking.sendToClients(new EnergySyncS2CPacket(ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
            }
        };

        this.data = new ContainerData() {
            @Override
            public int get(int index) { return containerDataGetter(index); }

            @Override
            public void set(int index, int value) { containerDataSetter(index, value);}

            @Override
            public int getCount() {
                return containerDataSize();
            }
        };
    }

    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {return false;}

    protected boolean hasEnoughEnergy() {
        return ENERGY_STORAGE.getEnergyStored() >= energyProcess;
    }

    protected void useEnergy() {
        ENERGY_STORAGE.extractEnergy(energyProcess, false);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
        ENERGY_STORAGE.onEnergyChanged();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
        lazyItemHandler.invalidate();
    }
    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putInt("EnergyEntity.processTick", processTick);
        nbt.putInt("EnergyEntity.processMaxTick", processMaxTick);
        nbt.put("EnergyEntity.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("EnergyEntity.energy", ENERGY_STORAGE.getEnergyStored());
        nbt.putBoolean("EnergyEntity.enabled", getBlockState().getValue(BaseSwitchableFacingBlock.ENABLED));
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        processTick = nbt.getInt("EnergyEntity.processTick");
        processMaxTick = nbt.getInt("EnergyEntity.processMaxTick");
        itemStackHandler.deserializeNBT(nbt.getCompound("EnergyEntity.inventory"));
        ENERGY_STORAGE.setEnergy(nbt.getInt("EnergyEntity.energy"));
        getBlockState().setValue(BaseSwitchableFacingBlock.ENABLED, nbt.getBoolean("EnergyEntity.enabled"));
    }

    public int getEnergyOperationCost() { return this.energyProcess; }

    public int getEnergyMaxTransfer() { return this.energyMaxTransfer; }

    public boolean isEnabled() {
        return getBlockState().getValue(BaseSwitchableFacingBlock.ENABLED).booleanValue();
    }

    public void setEnable(boolean enabled) {
        level.setBlock(getBlockPos(), getBlockState().setValue(BaseSwitchableFacingBlock.ENABLED, enabled), 4);
    }

    public void toggleEnable() {
        setEnable(!isEnabled());
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    protected int processMaxTickWithUpgrade() {
        ItemStack upgrade = itemStackHandler.getStackInSlot(getSlotUpgrade());
        if (upgrade != null && !upgrade.isEmpty() && upgrade.getItem() instanceof ItemProcessingUpgrade) {
            return processMaxTick/(((ItemProcessingUpgrade)upgrade.getItem()).getEfficiency());
        }
        return processMaxTick;
    }
}
