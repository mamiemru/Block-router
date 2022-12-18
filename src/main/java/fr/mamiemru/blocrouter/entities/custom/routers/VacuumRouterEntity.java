package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.VacuumRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.items.custom.ItemVacuumRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.VacuumPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VacuumRouterEntity extends BaseEntityEnergyRouter {

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
    public static final int SLOT_TELEPORTATION_CARD = 16;
    public static final int SLOT_PATTERN_SLOT_0 = 17;
    public static final int SLOT_PATTERN_SLOT_1 = 18;
    public static final int SLOT_PATTERN_SLOT_2 = 19;
    public static final int SLOT_PATTERN_SLOT_3 = 20;
    public static final int NUMBER_OF_SLOTS = 21;

    public static final double[] DROP_HEIGHT_CYCLE = {0.0D, 0.5D, 0.7D, 1D, 1.5D, 2D};

    private int dropHeightIndex = 0;

    @Override
    protected int containerDataGetter(int index) {
        return switch (index) {
            case 2 -> VacuumRouterEntity.this.dropHeightIndex;
            default -> super.containerDataGetter(index);
        };
    }

    @Override
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 2 -> VacuumRouterEntity.this.dropHeightIndex = value;
            default -> super.containerDataSetter(index, value);
        }
    }

    @Override
    protected int containerDataSize() {
        return 3;
    }

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemTeleportationSlot) {
            return slot == SLOT_TELEPORTATION_CARD;
        }
        return super.checkIsItemValid(slot, stack);
    }

    public VacuumRouterEntity(BlockPos pos, BlockState state) {super(EntitiesRegistry.VACUUM_ROUTER_ENTITY.get(), pos, state);}
    @Override
    protected int getSlotPatternSlot0() {return SLOT_PATTERN_SLOT_0;}
    @Override
    protected int getNumberOfPatternSlots() {return 4;}
    @Override
    protected int getSlotUpgrade() {return SLOT_UPGRADE;}
    @Override
    protected int getSlotInputSlot0() {return SLOT_INPUT_SLOT_0;}
    @Override
    protected int getSlotInputSlotN() {return SLOT_INPUT_SLOT_8;}
    @Override
    protected int getSlotOutputSlot0() {return SLOT_OUTPUT_SLOT_0;}
    @Override
    protected int getSlotOutputSlotN() {return SLOT_OUTPUT_SLOT_5;}

    @Override
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemVacuumRoutingPattern;
    }

    @Override
    public int getNumberOfSlots() {return NUMBER_OF_SLOTS;}
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new VacuumRouterMenu(pContainerId, pPlayerInventory, this, this.data);
    }
    @Override
    public Component getDisplayName() {return Component.literal("Vacuum Router");}

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        dropHeightIndex = nbt.getInt("VacuumRouterEntity.dropHeightIndex");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("VacuumRouterEntity.dropHeightIndex", dropHeightIndex);
    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {
        VacuumPattern vacuumPattern = (VacuumPattern) pattern;
        ItemStack teleportationCard = ItemTeleportationSlot.getTeleportationCardOrNull(itemStackHandler.getStackInSlot(SLOT_TELEPORTATION_CARD));

        if (teleportationCard == null) {
            return;
        }

        for (ItemEntity itemEntity : grabItemOnGround(getLevel(), ItemTeleportationSlot.getCoordinates(teleportationCard), vacuumPattern.getResult())) {
            if (vacuumPattern.getResult().stream().anyMatch(itemStack -> itemStack.getItem().equals(itemEntity.getItem().getItem()))) {
                ItemStack simulatedExtract = itemEntity.getItem();
                for (int outputSlot = getSlotOutputSlot0(); outputSlot <= getSlotOutputSlotN(); ++outputSlot) {
                    if (canTransferFromDistantToSelf(simulatedExtract, this.itemStackHandler, outputSlot)) {
                        ItemStack remainder = this.itemStackHandler.insertItem(outputSlot, simulatedExtract, true);
                        if (remainder.isEmpty()) {
                            this.itemStackHandler.insertItem(outputSlot, simulatedExtract, false);
                            itemEntity.discard();
                            break;
                        }
                    }
                }
            }
        }

    }
    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {
        VacuumPattern vacuumPattern = (VacuumPattern) pattern;
        ItemStack teleportationCard = ItemTeleportationSlot.getTeleportationCardOrNull(itemStackHandler.getStackInSlot(SLOT_TELEPORTATION_CARD));

        if (teleportationCard == null) {
            return;
        }

        if (!canCraft(itemStackHandler, vacuumPattern, getSlotInputSlot0(), getSlotInputSlotN())) {
            return;
        }

        List<ItemStack> itemStackList = extractIngredientList(this, this.itemStackHandler, vacuumPattern.getRows(), getSlotInputSlot0(), getSlotInputSlotN(), false);
        dropContentOnGround(this, itemStackList, ItemTeleportationSlot.getCoordinates(teleportationCard), getDropHeight());
    }

    @Override
    protected Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemVacuumRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
                ItemVacuumRoutingPattern.decodePatternTag(is) : null;
    }

    public double getDropHeight() {return DROP_HEIGHT_CYCLE[this.dropHeightIndex];}

    public int getDropHeightIndex() { return this.dropHeightIndex; }
}