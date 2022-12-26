package fr.mamiemru.blocrouter.entities.custom;


import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergyMachine;
import fr.mamiemru.blocrouter.gui.menu.menus.MobLootSorterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobLootSorterEntity extends BaseEntityEnergyMachine {

    public static final int SLOT_UPGRADE = 0;
    public static final int SLOT_FILTER = 1;
    public static final int SLOT_TRASH = 2;
    public static final int SLOT_INPUT_SLOT_MIN = 3;
    public static final int SLOT_INPUT_SLOT_MAX = 17;
    public static final int NUMBER_OF_SLOTS = 18;
    public static final int NUMBER_OF_VARS = 2;

    @Override
    protected boolean canExtractSide(int i) {
        return getSlotInputSlot0() <= i && i <= getSlotInputSlotN();
    }

    @Override
    protected boolean canInsertSide(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemFilter) {
            return slot == SLOT_FILTER;
        } else if (isItemStackToThrowAway(stack)) {
            return slot == SLOT_TRASH;
        } else if (SLOT_INPUT_SLOT_MIN <= slot && slot <= SLOT_INPUT_SLOT_MAX) {
            return true;
        }
        return super.checkIsItemValid(slot, stack);
    }

    public MobLootSorterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.MOB_LOOT_SORTER_ENTITY.get(), pos, state,
                BlockRouterConfig.OTHER_MACHINES_ENERGY_CAPACITY.get(),
                BlockRouterConfig.OTHER_MACHINES_ENERGY_MAX_TRANSFER.get(),
                BlockRouterConfig.OTHER_MACHINES_ENERGY_COST_PER_OPERATION.get()
        );
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected void handleExtraction() {

    }

    private boolean isItemStackToThrowAway(ItemStack is) {
        return is != null && (is.isDamaged() || ItemFilter.isItemOnFilter(itemStackHandler.getStackInSlot(SLOT_FILTER), is));
    }

    @Override
    protected void handleProcessing() {
        itemStackHandler.setStackInSlot(SLOT_TRASH, ItemStack.EMPTY);
        for (int slotIndex = SLOT_INPUT_SLOT_MIN; slotIndex <= SLOT_INPUT_SLOT_MAX; ++slotIndex) {
            ItemStack is = itemStackHandler.getStackInSlot(slotIndex);
            if (isItemStackToThrowAway(is)) {
                itemStackHandler.setStackInSlot(slotIndex, ItemStack.EMPTY);
            }
        }
        itemStackHandler.setStackInSlot(SLOT_TRASH, ItemStack.EMPTY);
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT_MIN;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT_MAX;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return -1;
    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Mob Loot Sorter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player pPlayer) {
        return new MobLootSorterMenu(id, inventory, this, this.data);
    }
}
