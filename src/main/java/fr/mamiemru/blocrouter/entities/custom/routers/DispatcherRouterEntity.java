package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.DispatcherRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemDispatcherRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.util.patterns.DispatcherPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DispatcherRouterEntity extends BaseEntityEnergyRouter {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_INPUT_SLOT_9 = 9;
    public static final int SLOT_INPUT_SLOT_10 = 10;
    public static final int SLOT_INPUT_SLOT_11 = 11;
    public static final int SLOT_OUTPUT_SLOT = 12;
    public static final int SLOT_OUTPUT_TELEPORTATION_CARD = 13;
    public static final int SLOT_UPGRADE = 14;
    public static final int SLOT_PATTERN_SLOT = 15;
    public static final int NUMBER_OF_SLOTS = 16;

    public DispatcherRouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.DISPATCHER_ROUTER_ENTITY.get(), pos, state);
    }

    @Override
    public Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemDispatcherRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
                ItemDispatcherRoutingPattern.decodePatternTag(is) : null;
    }

    @Override
    protected int getSlotPatternSlot0() {
        return SLOT_PATTERN_SLOT;
    }

    @Override
    protected int getNumberOfPatternSlots() {
        return 1;
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT_0;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT_11;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return SLOT_OUTPUT_SLOT;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return SLOT_OUTPUT_SLOT;
    }

    @Override
    protected int getSlotOutputTeleportationCard() { return SLOT_OUTPUT_TELEPORTATION_CARD; }

    @Override
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemDispatcherRoutingPattern;
    }

    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {
        useEnergy();
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_OUTPUT_TELEPORTATION_CARD);
        if (is != null && is.getItem() instanceof ItemTeleportationSlot && is.getCount() == 1 && is.hasTag()) {
            BlockPos blockPosToExtract = ItemTeleportationSlot.getCoordinates(is);
            IItemHandler inventoryToExtract = getItemHandler(level, blockPosToExtract, Direction.SOUTH);
            if (inventoryToExtract != null) {
                processExtractionFromDistantToSelf(inventoryToExtract, itemStackHandler, getSlotOutputSlot0(), getSlotOutputSlotN(), 64);
            }
        }
    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {
        DispatcherPattern dispatcherPattern;
        try {
            dispatcherPattern = (DispatcherPattern) pattern;
        } catch (Exception e) {
            return;
        }
        for (DispatcherPattern.DispatcherPatternIngredient ingredient : dispatcherPattern.getIngredientList()) {
            useEnergy();
            int selectedSlot = searchForItemStackInInventory(itemStackHandler, ingredient.getItemStack(), getSlotInputSlot0(), getSlotInputSlotN());
            IItemHandler targetInventory = getItemHandler(level, ingredient.getBlockPos(), Direction.UP);
            if (selectedSlot != -1 && targetInventory != null) {
                ItemStack itemsToTransfer = itemStackHandler.extractItem(selectedSlot, ingredient.getItemStack().getCount(), true);
                int selectedTargetSlot = canTransferFromSelfToDistant(itemsToTransfer, targetInventory);
                if (selectedTargetSlot != -1) {
                    targetInventory.insertItem(selectedTargetSlot, itemsToTransfer, false);
                    itemStackHandler.extractItem(selectedSlot, itemsToTransfer.getCount(), false);
                }
            }
        }
    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }


    public Component getDisplayName() {return Component.literal("Dispatcher Router");}

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DispatcherRouterMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
