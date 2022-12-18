package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.blocks.custom.routers.Router;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.WrappedHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.RouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemNormalRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.NormalRoutingPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
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

public class RouterEntity extends BaseEntityEnergy {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_OUTPUT_SLOT_0 = 9;
    public static final int SLOT_OUTPUT_SLOT_1 = 10;
    public static final int SLOT_OUTPUT_SLOT_2 = 11;
    public static final int SLOT_OUTPUT_SLOT_3 = 12;
    public static final int SLOT_OUTPUT_SLOT_4 = 13;
    public static final int SLOT_OUTPUT_SLOT_5 = 14;
    public static final int SLOT_UPGRADE = 15;
    public static final int SLOT_PATTERN_SLOT_0 = 16;
    public static final int SLOT_PATTERN_SLOT_1 = 17;
    public static final int SLOT_PATTERN_SLOT_2 = 18;
    public static final int SLOT_PATTERN_SLOT_3 = 19;
    public static final int NUMBER_OF_SLOTS = 20;

    protected final ContainerData data;

    private int processTick = 0;
    private int processMaxTick = 96;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (stack.getItem() instanceof ItemNormalRoutingPattern) {
                return SLOT_PATTERN_SLOT_0 <= slot && slot <= SLOT_PATTERN_SLOT_3;
            } else if (stack.getItem() instanceof ItemProcessingUpgrade) {
                return slot == SLOT_UPGRADE;
            } else if (SLOT_OUTPUT_SLOT_0 <= slot && slot <= SLOT_OUTPUT_SLOT_5) {
                return true;
            } else if (SLOT_INPUT_SLOT_0 <= slot && slot <= SLOT_INPUT_SLOT_8) {
                return true;
            }
            return false;
        }
    };

    private static boolean canExtractSide(int i) {
        return SLOT_OUTPUT_SLOT_0 <= i && i <= SLOT_OUTPUT_SLOT_5;
    }

    private static boolean canInsertSide(int slot, @NotNull ItemStack stack) {
        return SLOT_INPUT_SLOT_0 <= slot && slot <= SLOT_INPUT_SLOT_8;
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of( Direction.UP,   LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> false, (index, stack) -> SLOT_OUTPUT_SLOT_0 <= index && index <= SLOT_OUTPUT_SLOT_5)),
                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RouterEntity::canExtractSide, RouterEntity::canInsertSide)),
                    Direction.NORTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RouterEntity::canExtractSide, RouterEntity::canInsertSide)),
                    Direction.SOUTH,LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RouterEntity::canExtractSide, RouterEntity::canInsertSide)),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RouterEntity::canExtractSide, RouterEntity::canInsertSide)),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, RouterEntity::canExtractSide, RouterEntity::canInsertSide))
            );

    public RouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ROUTER_ENTITY.get(), pos, state, 64000, 1024, 32);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RouterEntity.this.processTick;
                    case 1 -> RouterEntity.this.processMaxTick;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RouterEntity.this.processTick = value;
                    case 1 -> RouterEntity.this.processMaxTick = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Router");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new RouterMenu(id, inventory, this, this.data);
    }

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
        nbt.put("RouterEntity.inventory", itemStackHandler.serializeNBT());
        nbt.putInt("RouterEntity.processTick", processTick);
        nbt.putInt("RouterEntity.processMaxTick", processMaxTick);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("RouterEntity.inventory"));
        processTick = nbt.getInt("RouterEntity.processTick");
        processMaxTick = nbt.getInt("RouterEntity.processMaxTick");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private static void forEveryMachines(@NotNull RouterEntity pEntity, @NotNull NormalRoutingPattern pattern, BlockEntity block_entity_reference) {

        Level level = pEntity.getLevel();
        BlockEntity block_entity_selected  = block_entity_reference;
        IItemHandler targetItemHandler = pEntity.getItemHandler(level, pEntity.getBlockPos(), Direction.UP);
        while (block_entity_selected != null && block_entity_reference.getClass().equals(block_entity_selected.getClass())) {

            BlockPos entity_pos = block_entity_selected.getBlockPos();

            if (canCraft(pEntity.itemStackHandler, pattern, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8)) {
                processExtractionFromSelfToDistantWithPattern(pEntity, pEntity.itemStackHandler, pattern, entity_pos, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8);
            }

            IItemHandler sourceItemHandler = pEntity.getItemHandler(level, entity_pos, Direction.DOWN);
            if (sourceItemHandler != null) {
                processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, SLOT_OUTPUT_SLOT_0, SLOT_OUTPUT_SLOT_5, 1);
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

    public static void tick(Level level, BlockPos pos, BlockState state, RouterEntity pEntity) {
        if (level.isClientSide() || !pEntity.isEnabled()) {
            return;
        }

        if (pEntity.hasEnoughEnergy()) {
            if (pEntity.processMaxTickWithUpgrade() <= pEntity.processTick) {
                BlockState block_reference = level.getBlockState(pos.above());
                BlockEntity block_entity_reference = level.getBlockEntity(pos.above());

                if (block_reference != null && block_reference.hasBlockEntity() && pEntity.itemStackHandler != null) {

                    for (int patternSlot = SLOT_PATTERN_SLOT_0; patternSlot <= SLOT_PATTERN_SLOT_3; ++patternSlot) {
                        pEntity.useEnergy();
                        ItemStack is = pEntity.itemStackHandler.getStackInSlot(patternSlot);
                        if (is != null && !is.isEmpty()) {
                            NormalRoutingPattern pattern = (NormalRoutingPattern) ItemNormalRoutingPattern.decodePatternTag(is);
                            if (pattern != null) {
                                forEveryMachines(pEntity, pattern, block_entity_reference);
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
        return RouterEntity.NUMBER_OF_SLOTS;
    }
}
