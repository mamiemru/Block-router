package fr.mamiemru.blocrouter.entities.custom.scatter;

import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergyMachine;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderScatterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderScatterEntity extends BaseEntityEnergyMachine {

    public static final int SLOT_INPUT_SLOT = 0;
    public static final int SLOT_INPUT_TRANSFER_MIN = 1;
    public static final int SLOT_INPUT_TRANSFER_MAX = 18;
    public static final int SLOT_UPGRADE = 19;

    public static final int NUMBER_OF_SLOTS = 20;
    public static final int NUMBER_OF_VARS = 2;

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return slot == SLOT_UPGRADE;
        } else if (stack.getItem() instanceof ItemTeleportationSlot) {
            return SLOT_INPUT_TRANSFER_MIN <= slot && slot <= SLOT_INPUT_TRANSFER_MAX;
        } else if (slot == SLOT_INPUT_SLOT) {
            return true;
        }
        return super.checkIsItemValid(slot, stack);
    }

    @Override
    protected boolean canExtractSide(int i) {
        return false;
    }

    public EnderScatterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_SCATTER_ENTITY.get(), pos, state,
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_CAPACITY.get(),
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_MAX_TRANSFER.get(),
                BlockRouterConfig.SCATTER_RETRIEVER_ENERGY_COST_PER_OPERATION.get()
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Scatter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderScatterMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected void handleExtraction() {

    }

    @Override
    protected void handleProcessing() {
        for (int transferSlotId = SLOT_INPUT_TRANSFER_MIN; transferSlotId <= SLOT_INPUT_TRANSFER_MAX; ++transferSlotId) {
            useEnergy();
            ItemStack teleportationSlot = itemStackHandler.getStackInSlot(transferSlotId);
            ItemStack itemsToTransfer   = itemStackHandler.extractItem(SLOT_INPUT_SLOT, getNumberOfItemsToTransferPerOperation(), true);
            if (teleportationSlot != null && !teleportationSlot.isEmpty() && teleportationSlot.getItem() instanceof ItemTeleportationSlot) {
                BlockPos blockPos = ItemTeleportationSlot.getCoordinates(teleportationSlot);
                IItemHandler targetItemHandlerOutput = getItemHandler(level, blockPos, Direction.UP);
                if (targetItemHandlerOutput != null) {
                    for (int idSlot = 0; idSlot < targetItemHandlerOutput.getSlots(); ++idSlot) {
                        if (processExtractionFromSelfToDistant(itemStackHandler, targetItemHandlerOutput, itemsToTransfer, SLOT_INPUT_SLOT, idSlot)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return -1;
    }

    private int getNumberOfItemsToTransferPerOperation() {
        /*
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_QUANTITY);
        if (is.getItem() instanceof ItemQuantityUpgrade) {
            return ItemQuantityUpgrade.getQuantity(is);
        }*/
        return 1;
    }

    @Override
    public int getNumberOfSlots() {
        return EnderScatterEntity.NUMBER_OF_SLOTS;
    }
}
