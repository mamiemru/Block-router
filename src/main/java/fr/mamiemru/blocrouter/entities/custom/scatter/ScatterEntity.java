package fr.mamiemru.blocrouter.entities.custom.scatter;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyMachine;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.ScatterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemProcessingUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScatterEntity extends BaseEntityEnergyMachine {

    public static final int SLOT_INPUT_SLOT = 0;
    public static final int SLOT_UPGRADE = 1;

    public static final int NUMBER_OF_SLOTS = 2;

    public static final int NUMBER_OF_VARS = 3;

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemProcessingUpgrade) {
            return SLOT_UPGRADE == slot;
        } else if (slot == SLOT_INPUT_SLOT) {
            return true;
        }
        return super.checkIsItemValid(slot, stack);
    }

    @Override
    protected boolean canExtractSide(int i) {
        return false;
    }

    public ScatterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.SCATTER_ENTITY.get(), pos, state, 32000, 1024, 32);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Scatter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ScatterMenu(id, inventory, this, this.data);
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
        BlockEntity block_entity_reference = level.getBlockEntity(getBlockPos().above());
        forEveryMachines(this, block_entity_reference);
        useEnergy();
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

    private static void forEveryMachines(@NotNull ScatterEntity pEntity, BlockEntity block_entity_reference) {

        Level level = pEntity.getLevel();
        BlockEntity block_entity_selected  = block_entity_reference;
        while (block_entity_selected != null && block_entity_reference.getClass().equals(block_entity_selected.getClass())) {

            BlockPos entity_pos = block_entity_selected.getBlockPos();

            IItemHandler targetItemHandlerOutput = pEntity.getItemHandler(level, entity_pos, Direction.UP);
            if (targetItemHandlerOutput != null) {
                ItemStack itemsToTransfer = pEntity.itemStackHandler.extractItem(SLOT_INPUT_SLOT, pEntity.getNumberOfItemsToTransferPerOperation(), true);
                for (int idSlot = 0; idSlot < targetItemHandlerOutput.getSlots(); ++idSlot) {
                    if (processExtractionFromSelfToDistant(pEntity.itemStackHandler, targetItemHandlerOutput, itemsToTransfer, SLOT_INPUT_SLOT, idSlot)) {
                        break;
                    }
                }
            }

            block_entity_selected = level.getBlockEntity(block_entity_selected.getBlockPos().above());
        }
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
        return ScatterEntity.NUMBER_OF_SLOTS;
    }
}
