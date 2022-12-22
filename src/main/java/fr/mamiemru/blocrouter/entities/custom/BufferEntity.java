package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.blocks.custom.routers.Router;
import fr.mamiemru.blocrouter.entities.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.WrappedHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.BufferMenu;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.BufferRendererS2CPacket;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BufferEntity extends BaseEntityWithMenuProvider {

    public static final int SLOT_STORAGE_INPUT = 0;
    public static final int SLOT_STORAGE_OUTPUT = 1;
    public static final int NUMBER_OF_SLOTS = 2;

    public static final int VAR_ITEM_COUNT = 0;
    public static final int VAR_ITEM_MAX_COUNT = 1;
    public static final int VAR_TOGGLE_INSERT = 2;
    public static final int NUMBER_OF_VARS = 3;

    private int itemCount = 0;
    private int itemMaxCount = 128;
    private int toggleInsert = 1;

    private ItemStack selectedItemStack = ItemStack.EMPTY;
    protected final ContainerData data;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == SLOT_STORAGE_INPUT) {
                return canInsert() && !stack.isEmpty();
            } else if (slot == SLOT_STORAGE_OUTPUT) {
                return canExtract() && stack.isEmpty();
            }
            return false;
        }
    };
    public BufferEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.BUFFER_ENTITY.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case VAR_ITEM_COUNT: return itemCount;
                    case VAR_ITEM_MAX_COUNT: return itemMaxCount;
                    case VAR_TOGGLE_INSERT: return toggleInsert;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case VAR_ITEM_COUNT: BufferEntity.this.itemCount = value;
                    case VAR_ITEM_MAX_COUNT: BufferEntity.this.itemMaxCount = value;
                    case VAR_TOGGLE_INSERT: BufferEntity.this.toggleInsert = value;
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_VARS;
            }
        };
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemStackHandler,
                            (index) -> itemStackHandler.isItemValid(index, ItemStack.EMPTY),
                            (index, stack) -> itemStackHandler.isItemValid(index, stack)))
            );

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return super.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(Router.FACING);

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
        CompoundTag itemStack = new CompoundTag();
        selectedItemStack.save(itemStack);
        nbt.put("selectedItemStack", itemStack);
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("buffer_entity.itemCount", itemCount);
        nbt.putInt("buffer_entity.itemMaxCount", itemMaxCount);;
        nbt.putInt("buffer_entity.toggleInsert", toggleInsert);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemCount = nbt.getInt("buffer_entity.itemCount");
        itemMaxCount = nbt.getInt("buffer_entity.itemMaxCount");
        toggleInsert = nbt.getInt("buffer_entity.toggleInsert");
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        selectedItemStack = ItemStack.of(nbt.getCompound("selectedItemStack"));
        if (getLevel() != null && !getLevel().isClientSide()) {
            ModNetworking.sendToClients(new BufferRendererS2CPacket(getBlockPos(), selectedItemStack));
        }
    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Item Buffer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BufferMenu(id, inventory, this, this.data);
    }

    private boolean canInsert() { return toggleInsert == 1; }
    private boolean canExtract() { return toggleInsert == 0; }
    private void toggleInsertion() { toggleInsert = 1; }
    private void toggleExtraction() { toggleInsert = 0; }

    public static void tick(Level level, BlockPos pos, BlockState state, BufferEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (pEntity.canInsert()) {
            ItemStack itemStack = pEntity.itemStackHandler.getStackInSlot(SLOT_STORAGE_INPUT);
            if (pEntity.itemMaxCount <= pEntity.itemCount) {
                pEntity.toggleExtraction();
            } else {
                if (pEntity.selectedItemStack.isEmpty()) {
                    pEntity.selectedItemStack = new ItemStack(itemStack.getItem(), 1);
                    ModNetworking.sendToClients(new BufferRendererS2CPacket(pos, pEntity.selectedItemStack));
                } else if (pEntity.selectedItemStack.getItem().equals(itemStack.getItem())) {
                    int canTransfer = Math.min(pEntity.itemMaxCount - pEntity.itemCount, itemStack.getCount());
                    if (canTransfer > 0) {
                        itemStack.setCount(itemStack.getCount() - canTransfer);
                        pEntity.itemCount += canTransfer;
                    }
                }
            }
        } else {
            ItemStack itemStack = pEntity.itemStackHandler.getStackInSlot(SLOT_STORAGE_OUTPUT);
            if (pEntity.itemCount == 0 && itemStack.isEmpty()) {
                pEntity.toggleInsertion();
                pEntity.selectedItemStack = ItemStack.EMPTY;
                ModNetworking.sendToClients(new BufferRendererS2CPacket(pos, pEntity.selectedItemStack));
            } else {
                int canTransfer = Math.min(itemStack.getMaxStackSize() - itemStack.getCount(), pEntity.itemCount);
                if (canTransfer > 0) {
                    ItemStack testStack = new ItemStack(pEntity.selectedItemStack.getItem(), canTransfer);
                    pEntity.itemCount -= canTransfer;
                    if (pEntity.itemStackHandler.getStackInSlot(SLOT_STORAGE_OUTPUT).isEmpty()) {
                        pEntity.itemStackHandler.setStackInSlot(SLOT_STORAGE_OUTPUT, testStack);
                    } else {
                        pEntity.itemStackHandler.getStackInSlot(SLOT_STORAGE_OUTPUT).setCount(
                                canTransfer + itemStack.getCount()
                        );
                    }
                }
            }
        }
    }

    public void setThreshold(int threshold) {
        if (itemMaxCount < threshold) {
            drops();
        }
        itemMaxCount = threshold;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStack getRenderStack() {
        return selectedItemStack;
    }

    public void setRenderStack(ItemStack itemStack) {
        selectedItemStack = new ItemStack(itemStack.getItem(), 1);
    }
}
