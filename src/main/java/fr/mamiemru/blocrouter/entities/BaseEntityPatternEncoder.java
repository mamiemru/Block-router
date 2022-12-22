package fr.mamiemru.blocrouter.entities;

import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
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

public abstract class BaseEntityPatternEncoder extends BaseEntityWithMenuProvider {

    protected final ContainerData data;

    public abstract void encodePattern();

    public abstract Pattern decodePattern(ItemStack is);

    public abstract int getNumberOfIngredientsSlots();

    public abstract int getSlotPatternSlot();

    protected abstract int getTeleportationCardSlot0();

    protected abstract int getItemSlot0();
    protected abstract int getItemSlotN();

    protected int getTeleportationCardSlotN() {
        return getTeleportationCardSlot0();
    }

    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemRoutingPattern) {
            return slot == getSlotPatternSlot();
        } else if (stack.getItem() instanceof ItemTeleportationSlot) {
            return getTeleportationCardSlot0() <= slot && slot <= getTeleportationCardSlotN();
        } else if (getItemSlot0() <= slot && slot <= getItemSlotN()) {
            return true;
        }
        return false;
    }

    protected final ItemStackHandler itemStackHandler = new ItemStackHandler(getNumberOfSlots()) {
        @Override
        protected void onContentsChanged(int slot) {setChanged();}

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) { return checkIsItemValid(slot, stack); }
    };

    protected int containerDataGetter(int index) {return 0;}
    protected void containerDataSetter(int index, int value) {}
    protected int containerDataSize() {
        return 0;
    }

    public BaseEntityPatternEncoder(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
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
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }
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
        nbt.put("inventory", itemStackHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            if (i < getItemSlot0() || getItemSlotN() < i) {
                inventory.setItem(i, itemStackHandler.getStackInSlot(i));
            }
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void dropSlot(int slot) {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        inventory.setItem(0, itemStackHandler.getStackInSlot(slot));
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PatternEncoderEntity pEntity) {}

    public void removeItemAt(int index) {
        itemStackHandler.setStackInSlot(index, ItemStack.EMPTY);
    }

    public void setItemAt(int index, ItemStack itemStack) {
        itemStackHandler.setStackInSlot(index, itemStack);
    }
}
