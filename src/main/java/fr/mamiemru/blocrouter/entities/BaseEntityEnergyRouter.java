package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.blocks.BaseFacingBlock;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemSlotRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemVacuumRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BaseEntityEnergyRouter extends BaseEntityEnergy {

    protected final ContainerData data;
    protected int processTick = 0;
    protected int processMaxTick = 96;
    protected List<Pattern> patterns = new ArrayList<>();

    protected abstract Pattern getCastedPattern(ItemStack is);
    protected abstract int getSlotPatternSlot0();
    protected abstract int getNumberOfPatternSlots();
    protected abstract int getSlotUpgrade();
    protected abstract int getSlotInputSlot0();
    protected abstract int getSlotInputSlotN();
    protected abstract int getSlotOutputSlot0();
    protected abstract int getSlotOutputSlotN();
    protected abstract boolean isPatternRight(Item item);

    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (isPatternRight(stack.getItem())) {
            return getSlotPatternSlot0() == slot;
        } else if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return slot == getSlotUpgrade();
        } else if (getSlotOutputSlot0() <= slot && slot <= getSlotOutputSlotN()) {
            return true;
        } else if (getSlotInputSlot0() <= slot && slot <= getSlotInputSlotN()) {
            return true;
        }
        return false;
    }

    protected final ItemStackHandler itemStackHandler = new ItemStackHandler(getNumberOfSlots()) {
        @Override
        protected void onContentsChanged(int slot) {
            if (getSlotPatternSlot0() <= slot && slot <= getSlotPatternSlot0() + getNumberOfSlots()) {
                loadPatterns();
            }
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) { return checkIsItemValid(slot, stack); }
    };

    private void loadPatterns() {
        if (getLevel() != null && !getLevel().isClientSide()) {
            System.out.println("Reload patterns");
            for (int patternSlot = 0; patternSlot < getNumberOfPatternSlots(); ++patternSlot) {
                ItemStack is = itemStackHandler.getStackInSlot(getSlotPatternSlot0() + patternSlot);
                if (is != null && !is.isEmpty()) {
                    BaseEntityEnergyRouter.this.patterns.add(patternSlot, getCastedPattern(is));
                } else {
                    BaseEntityEnergyRouter.this.patterns.add(patternSlot, null);
                }
            }
        }
    }

    protected boolean canExtractSide(int i) {
        return getSlotOutputSlot0() <= i && i <= getSlotOutputSlotN();
    }
    protected boolean canInsertSide(int slot, @NotNull ItemStack stack) {return getSlotInputSlot0() <= slot && slot <= getSlotInputSlotN();}

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of( Direction.UP,   LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, (index, stack) -> getSlotOutputSlot0() <= index && index <= getSlotOutputSlotN())),
                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergyRouter.this::canExtractSide, BaseEntityEnergyRouter.this::canInsertSide)),
                    Direction.NORTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergyRouter.this::canExtractSide, BaseEntityEnergyRouter.this::canInsertSide)),
                    Direction.SOUTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergyRouter.this::canExtractSide, BaseEntityEnergyRouter.this::canInsertSide)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergyRouter.this::canExtractSide, BaseEntityEnergyRouter.this::canInsertSide)),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, BaseEntityEnergyRouter.this::canExtractSide, BaseEntityEnergyRouter.this::canInsertSide))
            );

    protected int containerDataGetter(int index) {
        return switch (index) {
            case 0 -> BaseEntityEnergyRouter.this.processTick;
            case 1 -> BaseEntityEnergyRouter.this.processMaxTick;
            default -> 0;
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 0 -> BaseEntityEnergyRouter.this.processTick = value;
            case 1 -> BaseEntityEnergyRouter.this.processMaxTick = value;
        }
    }

    protected int containerDataSize() {
        return 2;
    }

    public BaseEntityEnergyRouter(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state, 64000, 1024, 32);
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
        nbt.put("BaseEntityEnergyRouter.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("BaseEntityEnergyRouter.processTick", processTick);
        nbt.putInt("BaseEntityEnergyRouter.processMaxTick", processMaxTick);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("BaseEntityEnergyRouter.inventory"));
        processTick = nbt.getInt("BaseEntityEnergyRouter.processTick");
        processMaxTick = nbt.getInt("BaseEntityEnergyRouter.processMaxTick");
        loadPatterns();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private int processMaxTickWithUpgrade() {
        ItemStack upgrade = itemStackHandler.getStackInSlot(getSlotUpgrade());
        if (upgrade != null && !upgrade.isEmpty() && upgrade.getItem() instanceof ItemProcessingUpgrade) {
            return processMaxTick/(((ItemProcessingUpgrade)upgrade.getItem()).getEfficiency());
        }
        return processMaxTick;
    }

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

        pEntity.patterns.forEach(System.out::println);

        if (!pEntity.patterns.isEmpty()) {
            for (Pattern pattern : pEntity.patterns) {
                forPattern(pEntity, pattern);
            }
        }

        pEntity.processTick = 0;
    }
}