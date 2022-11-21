package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.blocks.custom.EnderRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.EnderRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.util.Pattern;
import fr.mamiemru.blocrouter.util.PatternRow;
import net.minecraft.client.Minecraft;
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

public class EnderRouterEntity extends BaseEntityWithMenuProvider {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_INPUT_CARD_0 = 9;
    public static final int SLOT_INPUT_CARD_1 = 10;
    public static final int SLOT_INPUT_CARD_2 = 11;
    public static final int SLOT_INPUT_CARD_3 = 12;
    public static final int SLOT_INPUT_CARD_4 = 13;
    public static final int SLOT_INPUT_CARD_5 = 14;
    public static final int SLOT_INPUT_CARD_6 = 15;
    public static final int SLOT_INPUT_CARD_7 = 16;
    public static final int SLOT_INPUT_CARD_8 = 17;

    public static final int SLOT_OUTPUT_SLOT_0 = 18;
    public static final int SLOT_OUTPUT_SLOT_1 = 19;
    public static final int SLOT_OUTPUT_SLOT_2 = 20;
    public static final int SLOT_OUTPUT_CARD_0 = 21;
    public static final int SLOT_OUTPUT_CARD_1 = 22;
    public static final int SLOT_OUTPUT_CARD_2 = 23;
    public static final int SLOT_PATTERN_SLOT_0 = 24;
    public static final int SLOT_PATTERN_SLOT_1 = 25;
    public static final int SLOT_PATTERN_SLOT_2 = 26;
    public static final int SLOT_PATTERN_SLOT_3 = 27;
    public static final int NUMBER_OF_SLOTS = 28;

    protected final ContainerData data;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            if (stack.getItem() instanceof ItemRoutingPattern) {
                return SLOT_PATTERN_SLOT_0 <= slot && slot <= SLOT_PATTERN_SLOT_3;
            }
            if (stack.getItem() instanceof ItemTeleportationSlot) {
                return (SLOT_INPUT_CARD_0 <= slot && slot <= SLOT_INPUT_CARD_8) || (SLOT_OUTPUT_CARD_0 <= slot && slot <= SLOT_OUTPUT_CARD_2);
            }

            return true;
        }
    };

    private static boolean canExtract(int i) {
        return SLOT_OUTPUT_SLOT_0 <= i && i <= SLOT_OUTPUT_SLOT_2;
    }

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderRouterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderRouterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderRouterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderRouterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack))),
                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, EnderRouterEntity::canExtract,
                            (index, stack) -> itemStackHandler.isItemValid(index, stack)))
            );

    public EnderRouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_ROUTER_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                }
            }

            @Override
            public int getCount() {
                return 0;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Router");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderRouterMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return super.lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(EnderRouter.FACING);

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
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private static void forEveryTeleportationCards(@NotNull EnderRouterEntity pEntity, @NotNull Pattern pattern) {

        Direction[] directions = { Direction.UP, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.DOWN };

        if (Minecraft.getInstance().player != null)
            pattern.printToClientChat();

        if (canCraft(pEntity.itemStackHandler, pattern, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8)){

            print("Craftable");
            for (int i = SLOT_INPUT_CARD_0; i <= SLOT_INPUT_CARD_8; ++i) {

                int slotToCheck = i - SLOT_INPUT_CARD_0;
                print("Check slot card "+i+" and item in "+slotToCheck);
                ItemStack itemTeleportationSlot = pEntity.itemStackHandler.getStackInSlot(i);
                if (itemTeleportationSlot != null && itemTeleportationSlot.getItem() instanceof ItemTeleportationSlot) {
                    PatternRow ingredientToFind = pattern.getRows().get(slotToCheck);
                    BlockPos entity_pos = ItemTeleportationSlot.getCoordinates(itemTeleportationSlot);
                    IItemHandler targetInventory = getItemHandler(pEntity.getLevel(),entity_pos, directions[ingredientToFind.axe]);
                    print("IngredientToFind "+ingredientToFind.is.getDescriptionId());
                    print("blockPosition "+entity_pos.toShortString());

                    for (int slotInput = SLOT_INPUT_SLOT_0; slotInput <= SLOT_INPUT_SLOT_8; ++slotInput) {
                        print("SlotInput "+slotInput);
                        ItemStack itemsToTransfer = pEntity.itemStackHandler.extractItem(slotToCheck, ingredientToFind.is.getCount(), true);
                        if (targetInventory != null && itemStackAreEqual(ingredientToFind.is, itemsToTransfer)) {
                            processExtractionFromSelfToDistant(pEntity.itemStackHandler, targetInventory, ingredientToFind.is, slotInput, ingredientToFind.slot);
                            break;
                        }
                    }
                }
            }
        }

        for (int i = SLOT_OUTPUT_CARD_0; i <= SLOT_OUTPUT_CARD_2; ++i) {
            ItemStack itemTeleportationSlot = pEntity.itemStackHandler.getStackInSlot(i);
            if (itemTeleportationSlot != null && itemTeleportationSlot.getItem() instanceof ItemTeleportationSlot) {
                BlockPos entity_pos = ItemTeleportationSlot.getCoordinates(itemTeleportationSlot);
                IItemHandler sourceInventory = getItemHandler(pEntity.getLevel(),entity_pos, Direction.DOWN);

                if (sourceInventory != null) {
                    for (int availableOutputSlots = 0; availableOutputSlots < sourceInventory.getSlots(); ++availableOutputSlots) {
                        int testSlotSize = sourceInventory.getSlotLimit(availableOutputSlots);
                        ItemStack simulatedExtract = sourceInventory.extractItem(availableOutputSlots, testSlotSize, true);
                        processExtractionFromSelfToDistant(sourceInventory, pEntity.itemStackHandler, simulatedExtract, availableOutputSlots, i - 3);
                    }
                }
            }
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderRouterEntity pEntity) {
        if (level.isClientSide() || !state.getValue(EnderRouter.ENABLED)) {
            return;
        }

        if (pEntity.itemStackHandler != null) {

            for (int patternSlot = SLOT_PATTERN_SLOT_0; patternSlot <= SLOT_PATTERN_SLOT_3; ++patternSlot) {
                ItemStack is = pEntity.itemStackHandler.getStackInSlot(patternSlot);
                if (is != null && !is.isEmpty()) {
                    Pattern pattern = ItemRoutingPattern.decodePatternTag(is);
                    if (pattern != null) {
                        forEveryTeleportationCards(pEntity, pattern);
                    }
                }
            }
        }
    }

    @Override
    public int getNumberOfSlots() {
        return EnderRouterEntity.NUMBER_OF_SLOTS;
    }


}