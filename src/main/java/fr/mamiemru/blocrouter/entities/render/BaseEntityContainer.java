package fr.mamiemru.blocrouter.entities.render;

import fr.mamiemru.blocrouter.blocks.BaseFacingBlock;
import fr.mamiemru.blocrouter.blocks.BaseSwitchableFacingBlock;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.WrappedHandler;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class BaseEntityContainer extends BlockEntity {

    protected final ContainerData data;
    protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected abstract int getSlotInputSlot0();
    protected abstract int getSlotInputSlotN();
    protected abstract int getSlotOutputSlot0();
    protected abstract int getSlotOutputSlotN();
    protected abstract void onSlotContentChanged(int slot);
    protected abstract int getNumberOfSlots();

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
            Map.of(
                    Direction.NORTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityContainer.this::canExtractSide, BaseEntityContainer.this::canInsertSide)),
                    Direction.SOUTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityContainer.this::canExtractSide, BaseEntityContainer.this::canInsertSide)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityContainer.this::canExtractSide, BaseEntityContainer.this::canInsertSide)),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityContainer.this::canExtractSide, BaseEntityContainer.this::canInsertSide))
            );


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(BaseFacingBlock.FACING);

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }
        return super.getCapability(cap, side);
    }

    protected int containerDataGetter(int index) { return 0;}
    protected void containerDataSetter(int index, int value) {}
    protected int containerDataSize() { return 0; }
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {return false;}

    public BaseEntityContainer(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);

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
        nbt.put("BaseEntityContainer.inventory", itemStackHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("BaseEntityContainer.inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
}
